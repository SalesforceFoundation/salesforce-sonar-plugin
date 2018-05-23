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

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.profiles.XMLProfileParser;
import org.sonar.api.rules.Rule;
import org.sonar.api.utils.ValidationMessages;

import org.sonar.salesforce.SalesforcePlugin;

import java.io.InputStreamReader;

public class SalesforceProfile extends ProfileDefinition {

    @Override
    public RulesProfile createProfile(ValidationMessages validation) {
		RulesProfile profile = RulesProfile.create("Salesforce", SalesforcePlugin.LANGUAGE_KEY);
		profile.activateRule(Rule.create(SalesforcePlugin.REPOSITORY_KEY, "GenericSalesforcePmdViolation"), null);

		return profile;

    }

}
