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

package org.sonar.salesforce.rule;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.salesforce.SalesforcePlugin;
import org.sonar.api.server.rule.RulesDefinition;
import java.util.Iterator;
import java.util.List;


public class SalesforceProfile implements BuiltInQualityProfilesDefinition {

	public void define(Context context) {
		NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile("Salesforce Rules", SalesforcePlugin.LANGUAGE_KEY);
		profile.setDefault(true);

		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "GenericSalesforcePmdViolation");

		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "IfElseStmtsMustUseBraces");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "WhileLoopsMustUseBraces");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "ApexXSSFromURLParam");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "ApexSuggestUsingNamedCred");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "AvoidSoslInLoops");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "ApexSOQLInjection");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "ApexXSSFromEscapeFalse");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "EmptyIfStmt");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "ExcessiveClassLength");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "ApexBadCrypto");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "ApexUnitTestShouldNotUseSeeAllDataTrue");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "NcssMethodCount");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "StdCyclomaticComplexity");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "ApexCSRF");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "AvoidLogicInTrigger");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "ExcessiveParameterList");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "EmptyCatchBlock");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "NcssConstructorCount");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "EmptyWhileStmt");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "ApexUnitTestClassShouldHaveAsserts");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "ClassNamingConventions");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "ForLoopsMustUseBraces");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "CyclomaticComplexity");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "ApexInsecureEndpoint");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "EmptyStatementBlock");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "AvoidGlobalModifier");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "EmptyTryOrFinallyBlock");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "ApexCRUDViolation");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "ApexOpenRedirect");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "ApexSharingViolations");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "AvoidDirectAccessTriggerMap");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "AvoidDmlStatementsInLoops");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "IfStmtsMustUseBraces");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "VariableNamingConventions");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "MethodWithSameNameAsEnclosingClass");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "ExcessivePublicCount");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "NcssTypeCount");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "MethodNamingConventions");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "AvoidSoqlInLoops");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "AvoidHardcodingId");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "AvoidDeeplyNestedIfStmts");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "TooManyFields");
		profile.activateRule(SalesforcePlugin.REPOSITORY_KEY, "ApexDangerousMethods");
		profile.done();
	}

}