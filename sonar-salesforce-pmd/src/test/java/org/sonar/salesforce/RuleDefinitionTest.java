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

import java.util.List;
import java.util.Iterator;

import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.salesforce.SalesforcePlugin;
import org.sonar.salesforce.rule.GenericSalesforceRuleDefinition;

import static org.fest.assertions.Assertions.assertThat;


public class RuleDefinitionTest {

  @Test
  public void test() {
    RulesDefinition.Context context = new RulesDefinition.Context();
    GenericSalesforceRuleDefinition ruleDefinition = new GenericSalesforceRuleDefinition();

    ruleDefinition.define(context);

    RulesDefinition.Repository repository = context.repository(SalesforcePlugin.REPOSITORY_KEY);

    assertThat(repository.name()).isEqualTo("Salesforce Analyzer");

    List<RulesDefinition.Rule> rules = repository.rules();
    // assertThat(rules.get(0).key()).isEqualTo("IfElseStmtsMustUseBraces");

    for (Iterator<RulesDefinition.Rule> i = rules.iterator(); i.hasNext();) {
       RulesDefinition.Rule rule = i.next();
         System.out.println(rule.key());
    }



    assertThat(rules.size()).isEqualTo(44);
  }

}