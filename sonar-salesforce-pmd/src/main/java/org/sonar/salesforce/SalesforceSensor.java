/*
 * Salesforce Plugin for SonarQube
 * Copyright (C) 2018-2017 Salesforce.org
 * esteele@salesforce.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.sonar.salesforce;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import javax.xml.parsers.ParserConfigurationException;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.sensor.issue.internal.DefaultIssueLocation;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.api.utils.log.Profiler;
import org.sonar.salesforce.base.SalesforceConstants;
import org.sonar.salesforce.base.SalesforceMetrics;
import org.sonar.salesforce.parser.ReportParser;
import org.sonar.salesforce.parser.XmlReportFile;
import org.sonar.salesforce.parser.element.Analysis;
import org.sonar.salesforce.parser.element.File;
import org.sonar.salesforce.parser.element.Violation;

import org.xml.sax.SAXException;


public class SalesforceSensor implements Sensor {
    private static final Logger LOGGER = Loggers.get(SalesforceSensor.class);
    private static final String SENSOR_NAME = "Salesforce";

    private final FileSystem fileSystem;
    private final PathResolver pathResolver;

    private int totalFiles;
    private int violationCount;
    private int criticalIssuesCount;
    private int majorIssuesCount;
    private int minorIssuesCount;

    public SalesforceSensor(FileSystem fileSystem, PathResolver pathResolver) {
        this.fileSystem = fileSystem;
        this.pathResolver = pathResolver;
    }

    @Override
    public String toString() {
        return SENSOR_NAME;
    }

    @Override
    public void describe(SensorDescriptor sensorDescriptor) {
        sensorDescriptor.name(SENSOR_NAME);
    }

	// private String getXmlReport(SensorContext context) {
	// 	XmlReportFile report = new XmlReportFile(context.settings(), fileSystem, this.pathResolver);
	// 	// File reportFile = report.getFile(DependencyCheckConstants.HTML_REPORT_PATH_PROPERTY);
	// 	// if (reportFile == null || !reportFile.exists() || !reportFile.isFile() || !reportFile.canRead()) {
	// 	// 	return null;
	// 	// }
	// 	// int len = (int) reportFile.length();
	// 	// try (FileInputStream reportFileInputStream = new FileInputStream(reportFile)) {
	// 	// 	byte[] readBuffer = new byte[len];
	// 	// 	reportFileInputStream.read(readBuffer, 0, len);
	// 	// 	return new String(readBuffer);
	// 	// } catch (IOException e) {
	// 	// 	LOGGER.error("", e);
	// 	// 	return null;
	// 	// }
	// }

    private Analysis parseAnalysis(SensorContext context) throws IOException, ParserConfigurationException, SAXException {
        XmlReportFile report = new XmlReportFile(context.settings(), fileSystem, this.pathResolver);

        try (InputStream stream = report.getInputStream(SalesforceConstants.REPORT_PATH_PROPERTY)) {
        	return new ReportParser().parse(stream);
        }
    }

    private void saveMeasures(SensorContext context) {
    	LOGGER.debug("PMD: In saveMeasures");
        context.newMeasure().forMetric(SalesforceMetrics.HIGH_SEVERITY_VIOLATIONS).on(context.module()).withValue(criticalIssuesCount).save();
        context.newMeasure().forMetric(SalesforceMetrics.MEDIUM_SEVERITY_VIOLATIONS).on(context.module()).withValue(majorIssuesCount).save();
        context.newMeasure().forMetric(SalesforceMetrics.LOW_SEVERITY_VIOLATIONS).on(context.module()).withValue(minorIssuesCount).save();
        context.newMeasure().forMetric(SalesforceMetrics.TOTAL_VIOLATIONS).on(context.module()).withValue(violationCount).save();
    }

    private String formatDescription(File file, Violation violation) {
        StringBuilder sb = new StringBuilder();
        sb.append(violation.getDescription());
        return sb.toString();
    }

    public static Severity priorityToSeverity(String priority, Integer critical, Integer major) {
        int score = Integer.parseInt(priority);
        if (critical > 0 && score <= critical) {
            return Severity.CRITICAL;
        } else if (major > 0 && score <= major) {
            return Severity.MAJOR;
        } else {
            return Severity.MINOR;
        }
    }

    private void incrementCount(Severity severity) {
        switch (severity) {
            case CRITICAL:
                this.criticalIssuesCount++;
                break;
            case MAJOR:
                this.majorIssuesCount++;
                break;
            case MINOR:
                this.minorIssuesCount++;
                break;
            default:
                LOGGER.debug("Unknown severity {}", severity);
        }
    }

    private void saveMetricOnFile(SensorContext context, InputFile inputFile, Metric<Serializable> metric, double value) {
        if (inputFile != null) {
            context.newMeasure().on(inputFile).forMetric(metric).withValue(value);
        }
    }

    private void addIssue(SensorContext context, File file, InputFile inputFile, Violation violation) {
        try {
            Severity severity = priorityToSeverity(violation.getPriority(), 2, 4);
            LOGGER.debug("Creating issue for rule {}:{} for file {}", SalesforcePlugin.REPOSITORY_KEY, violation.getRule(), file.getPath());


            NewIssue issue = context.newIssue()
                    .forRule(RuleKey.of(SalesforcePlugin.REPOSITORY_KEY, violation.getRule()));

            Integer sCol = Integer.parseInt(violation.getBeginColumn());
            Integer eCol = Integer.parseInt(violation.getEndColumn());
            Integer sLine = Integer.parseInt(violation.getBeginLine());
            Integer eLine = Integer.parseInt(violation.getEndLine());

            try {
                // Attemmpt to add a full location
                NewIssueLocation location = issue.newLocation()
                    .on(inputFile)
                    .at(inputFile.newRange(sLine, sCol, eLine, eCol))
                    .message(formatDescription(file, violation));
                issue.at(location);
            } catch (IllegalArgumentException e) {
                // Otherwise, just log the line
                LOGGER.debug("Failed to create an exact location, attempting to register only the line.");
                NewIssueLocation location = issue.newLocation()
                    .on(inputFile)
                    .at(inputFile.selectLine(sLine))
                    .message(formatDescription(file, violation));
                issue.at(location);
            }

            issue.overrideSeverity(severity);
            issue.save();
            incrementCount(severity);
        } catch (Exception e) {
            LOGGER.debug("Exception {} while parsing {} {}:{}", e, file.getPath(), violation.getRule());
        }
    }


    private void addIssues(SensorContext context, Analysis analysis) {
        if (analysis.getFiles() == null) {
            return;
        }
        for (File file : analysis.getFiles()) {
            InputFile testFile = fileSystem.inputFile(
                    fileSystem.predicates().hasPath(file.getPath())
            );

            int fileViolationCount = file.getViolations().size();

            if (fileViolationCount > 0) {
                fileViolationCount++;
            }
            saveMetricOnFile(context, testFile, SalesforceMetrics.TOTAL_VIOLATIONS, (double) fileViolationCount);

            for (Violation violation : file.getViolations()) {
                addIssue(context, file, testFile, violation);
                violationCount++;
            }
        }
    }

    @Override
    public void execute(SensorContext sensorContext) {
        Profiler profiler = Profiler.create(LOGGER);
        profiler.startInfo("Process Salesforce PMD report");
        try {
            Analysis analysis = parseAnalysis(sensorContext);
            this.totalFiles = analysis.getFiles().size();
            addIssues(sensorContext, analysis);
        } catch (FileNotFoundException e) {
            LOGGER.debug("Analysis aborted due to missing report file", e);
        } catch (Exception e) {
            throw new RuntimeException("Cannot process Salesforce-PMD report.", e);
        } finally {
            profiler.stopInfo();
        }
        saveMeasures(sensorContext);
    }

}