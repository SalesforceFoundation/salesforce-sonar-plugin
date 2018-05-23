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
package org.sonar.salesforce.parser;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.codehaus.staxmate.SMInputFactory;
import org.sonar.salesforce.parser.element.*;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.api.utils.log.Profiler;


public class ReportParser {

    private static final Logger LOGGER = Loggers.get(ReportParser.class);

    public Analysis parse(InputStream inputStream) {
        LOGGER.debug("PMD: In parse");

        XMLInputFactory xmlFactory = XMLInputFactory.newInstance();
        xmlFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
        xmlFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.FALSE);
        xmlFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlFactory.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
        SMInputFactory inputFactory = new SMInputFactory(xmlFactory);

        try {
            SMHierarchicCursor rootC = inputFactory.rootElementCursor(inputStream);
            rootC.advance(); // <analysis>

            ScanInfo scanInfo = processScanInfo(rootC);
            Collection<File> files = new ArrayList<>();

            SMInputCursor childCursor = rootC.childCursor();

            while (childCursor.getNext() != null) {
                String nodeName = childCursor.getLocalName();
                if ("file".equals(nodeName)) {
                    File file = processFile(childCursor);
                    files.add(file);
                }
            }
            return new Analysis(scanInfo, files);
        } catch (XMLStreamException e) {
            throw new IllegalStateException("XML is not valid", e);
        }
    }

    private ScanInfo processScanInfo(SMInputCursor siC) throws XMLStreamException {
        ScanInfo scanInfo = new ScanInfo();
        scanInfo.setEngineVersion(StringUtils.trim(siC.getAttrValue("version")));
        scanInfo.setScanDate(StringUtils.trim(siC.getAttrValue("timestamp")));
        return scanInfo;
    }

    private File processFile(SMInputCursor fileC) throws XMLStreamException {
        LOGGER.debug("PMD: In processFile");
        File file = new File();
        Collection<Violation> violations = new ArrayList<>();

        file.setPath(StringUtils.trim(fileC.getAttrValue("name")));

        SMInputCursor childCursor = fileC.childCursor();
        while (childCursor.getNext() != null) {
            String nodeName = childCursor.getLocalName();
            if ("violation".equals(nodeName)) {
                Violation violation = new Violation();
                violation.setRule(StringUtils.trim(childCursor.getAttrValue("rule")));
                violation.setRuleset(StringUtils.trim(childCursor.getAttrValue("ruleset")));
                violation.setBeginLine(StringUtils.trim(childCursor.getAttrValue("beginline")));
                violation.setEndLine(StringUtils.trim(childCursor.getAttrValue("endline")));
                violation.setBeginColumn(StringUtils.trim(childCursor.getAttrValue("begincolumn")));
                violation.setEndColumn(StringUtils.trim(childCursor.getAttrValue("endcolumn")));
                violation.setExternalInfoUrl(StringUtils.trim(childCursor.getAttrValue("externalInfoUrl")));
                violation.setPriority(StringUtils.trim(childCursor.getAttrValue("priority")));
                violation.setDescription(StringUtils.trim(childCursor.collectDescendantText(false)));
                violations.add(violation);
            }

        }
        file.setViolations(violations);
        return file;
    }
}