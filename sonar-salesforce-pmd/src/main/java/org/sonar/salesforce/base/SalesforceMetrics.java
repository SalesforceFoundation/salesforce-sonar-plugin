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
package org.sonar.salesforce.base;

import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public final class SalesforceMetrics implements Metrics {

    private static final String DOMAIN = "Salesforce";

    private static final String TOTAL_VIOLATIONS_KEY = "total_violations";
    private static final String HIGH_SEVERITY_VIOLATIONS_KEY = "high_severity_violations";
    private static final String MEDIUM_SEVERITY_VIOLATIONS_KEY = "medium_severity_violations";
    private static final String LOW_SEVERITY_VIOLATIONS_KEY = "low_severity_violations";

    public static final Metric<Serializable> HIGH_SEVERITY_VIOLATIONS = new Metric.Builder(HIGH_SEVERITY_VIOLATIONS_KEY, "High Severity Violations", Metric.ValueType.INT)
            .setDescription("High Severity Violations")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(false)
            .setDomain(SalesforceMetrics.DOMAIN)
            .setBestValue(0.0)
            .setHidden(false)
            .create();

    public static final Metric<Serializable> MEDIUM_SEVERITY_VIOLATIONS = new Metric.Builder(MEDIUM_SEVERITY_VIOLATIONS_KEY, "Medium Severity Violations", Metric.ValueType.INT)
            .setDescription("Medium Severity Violations")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(false)
            .setDomain(SalesforceMetrics.DOMAIN)
            .setBestValue(0.0)
            .setHidden(false)
            .create();

    public static final Metric<Serializable> LOW_SEVERITY_VIOLATIONS = new Metric.Builder(LOW_SEVERITY_VIOLATIONS_KEY, "Low Severity Violations", Metric.ValueType.INT)
            .setDescription("Low Severity Violations")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(false)
            .setDomain(SalesforceMetrics.DOMAIN)
            .setBestValue(0.0)
            .setHidden(false)
            .create();

    public static final Metric<Serializable> TOTAL_VIOLATIONS = new Metric.Builder(TOTAL_VIOLATIONS_KEY, "Total Violations", Metric.ValueType.INT)
            .setDescription("Total Violations")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(false)
            .setDomain(SalesforceMetrics.DOMAIN)
            .setBestValue(0.0)
            .setHidden(false)
            .create();


    @Override
    public List<Metric> getMetrics() {
        return Arrays.asList(
            SalesforceMetrics.HIGH_SEVERITY_VIOLATIONS,
            SalesforceMetrics.MEDIUM_SEVERITY_VIOLATIONS,
            SalesforceMetrics.LOW_SEVERITY_VIOLATIONS,
            SalesforceMetrics.TOTAL_VIOLATIONS
        );
    }
}