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

import org.junit.Test;
import org.sonar.salesforce.parser.ReportParser;
import org.sonar.salesforce.parser.element.*;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import static org.fest.assertions.Assertions.assertThat;

public class ReportParserTest {

    @Test
    public void parseReport() throws Exception {
        ReportParser parser = new ReportParser();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("report/pmd-report.xml");
        Analysis analysis = parser.parse(inputStream);
        assertThat(analysis.getScanInfo().getEngineVersion()).isEqualTo("1.2.3");
        assertThat(analysis.getScanInfo().getScanDate()).isEqualTo("2018-05-16T17:53:33.275");

        Collection<File> files = analysis.getFiles();
        assertThat(files).hasSize(3);
        Iterator iterator = files.iterator();
        File file = (File) iterator.next();

        assertThat(file.getPath()).isEqualTo("/path/to/src/classes/AAGH_GithubRequestHandler.cls");
        Collection<Violation> violations = file.getViolations();
        assertThat(violations).hasSize(2);
        Iterator violationIterator = violations.iterator();
        Violation violation = (Violation) violationIterator.next();
		assertThat(violation.getRule()).isEqualTo("AvoidGlobalModifier");
		assertThat(violation.getDescription()).isEqualTo("Avoid using global modifier");
		assertThat(violation.getRuleset()).isEqualTo("Best Practices");
		assertThat(violation.getPriority()).isEqualTo("3");
		assertThat(violation.getBeginLine()).isEqualTo("36");
		assertThat(violation.getEndLine()).isEqualTo("194");
		assertThat(violation.getBeginColumn()).isEqualTo("30");
		assertThat(violation.getEndColumn()).isEqualTo("2");
		assertThat(violation.getExternalInfoUrl()).isEqualTo("http://pmd.sourceforge.net/snapshot/pmd_rules_apex_bestpractices.html#avoidglobalmodifier");
    }
}