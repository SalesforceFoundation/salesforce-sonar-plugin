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

import org.sonar.api.Plugin;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.salesforce.base.SalesforceConstants;
import org.sonar.salesforce.base.SalesforceMetrics;
import org.sonar.salesforce.rule.ApexLanguage;
import org.sonar.salesforce.rule.SalesforceProfile;
import org.sonar.salesforce.rule.GenericSalesforceRuleDefinition;

public class SalesforcePlugin implements Plugin {

    public static final String REPOSITORY_KEY = "Salesforce";
    public static final String LANGUAGE_KEY = "apex";

    @Override
    public void define(Context context) {
        context.addExtensions(SalesforceSensor.class,
                              SalesforceMetrics.class,
                              SalesforceProfile.class,
                              GenericSalesforceRuleDefinition.class,
                              ApexLanguage.class);

        context.addExtension(
                PropertyDefinition.builder(SalesforceConstants.REPORT_PATH_PROPERTY)
                        .subCategory("Paths")
                        .name("PMD report path")
                        .description("path to the pmd xml results file")
                        .defaultValue("${WORKSPACE}/pmd-report.xml")
                        .build()
        );
    }

}