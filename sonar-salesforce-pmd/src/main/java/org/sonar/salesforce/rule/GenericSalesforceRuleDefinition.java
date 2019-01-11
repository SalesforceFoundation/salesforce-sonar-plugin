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
import org.sonar.salesforce.SalesforcePlugin;

// import org.sonar.api.config.Settings;
import org.sonar.api.server.rule.RulesDefinition;
// import org.sonar.api.rule.RuleKey;
// import org.sonar.api.server.rule.RuleScope;
import org.sonar.api.rule.RuleStatus;
// import org.sonar.api.server.rule.RuleParamType;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
// import org.sonar.api.server.ServerSide;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import java.io.InputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.codehaus.staxmate.SMInputFactory;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.batch.rule.internal.NewRule;



public class GenericSalesforceRuleDefinition implements RulesDefinition {

    private static final Logger LOGGER = Loggers.get(GenericSalesforceRuleDefinition.class);
    // private final Settings settings;

    @Override
    public void define(Context context) {

        // this.settings = context.settings();

        LOGGER.debug("PMD: In GenericSalesforceRuleDefinition define");
        NewRepository repository = context.createRepository(SalesforcePlugin.REPOSITORY_KEY, SalesforcePlugin.LANGUAGE_KEY).setName("Salesforce Analyzer");
        LOGGER.debug("PMD: Created repo");

        // repository.createRule("GenericSalesforcePmdViolation")
        // .setName("Generic Salesforce Pmd Violation")
        // .setHtmlDescription("Generic rule")
        // .setSeverity(Severity.MINOR);
        // .setTags("style", )


        addRulesFromPmdRuleset(repository, "/org/sonar/salesforce/rulesets/apex/bestpractices.xml", RuleType.BUG);
        addRulesFromPmdRuleset(repository, "/org/sonar/salesforce/rulesets/apex/codestyle.xml", RuleType.CODE_SMELL);
        addRulesFromPmdRuleset(repository, "/org/sonar/salesforce/rulesets/apex/design.xml", RuleType.CODE_SMELL);
        addRulesFromPmdRuleset(repository, "/org/sonar/salesforce/rulesets/apex/errorprone.xml", RuleType.BUG);
        addRulesFromPmdRuleset(repository, "/org/sonar/salesforce/rulesets/apex/performance.xml", RuleType.BUG);
        addRulesFromPmdRuleset(repository, "/org/sonar/salesforce/rulesets/apex/security.xml", RuleType.VULNERABILITY);


       repository.done();

   }

    public static String priorityToSeverity(String priority, Integer critical, Integer major) {
        int score = Integer.parseInt(priority);
        if (critical > 0 && score <= critical) {
            return Severity.CRITICAL;
        } else if (major > 0 && score <= major) {
            return Severity.MAJOR;
        } else {
            return Severity.MINOR;
        }
    }

    public void addRulesFromPmdRuleset(NewRepository repository, String resourcePath, RuleType ruleType){
        InputStream inputStream = getClass().getResourceAsStream(resourcePath);

        XMLInputFactory xmlFactory = XMLInputFactory.newInstance();
        xmlFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
        xmlFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.FALSE);
        xmlFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlFactory.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
        SMInputFactory inputFactory = new SMInputFactory(xmlFactory);

        LOGGER.debug("Reading rule file");

        try {
            SMHierarchicCursor rootC = inputFactory.rootElementCursor(inputStream);
            rootC.advance(); // ruleset

            SMInputCursor ruleCursor = rootC.childCursor();

            while (ruleCursor.getNext() != null) {
                String nodeName = ruleCursor.getLocalName();
                if ("rule".equals(nodeName)) {

                    String name = StringUtils.trim(ruleCursor.getAttrValue("name"));
                    String message = StringUtils.trim(ruleCursor.getAttrValue("message"));
                    String url = StringUtils.trim(ruleCursor.getAttrValue("externalInfoUrl"));
                    String description = "";
                    String priority = "";

                    SMInputCursor ruleData = ruleCursor.childCursor();

                    while (ruleData.getNext() != null) {
                        String childName = ruleData.getLocalName();
                        if ("description".equals(childName)) {
                            description = StringUtils.trim(ruleData.collectDescendantText(false));
                        } else if ("priority".equals(childName)) {
                            priority = StringUtils.trim(ruleData.collectDescendantText(false));
                        }
                        // example
                        // tags ?
                    }
                    // String settingsKey = "sonar.salesforce.rule." + name + ".priority";
                    // LOGGER.debug("Looking for setting {}", settingsKey);
                    // if (settings.hasKey(settingsKey)){
                    //     LOGGER.debug("Found priority override for {}", name);
                    //     priority = settings.getString(settingsKey);
                    // }


                    NewRule rule = repository.createRule(name)
                        .setName(message)
                        .setType(ruleType)
                        .setSeverity(priorityToSeverity(priority, 2, 4))
                        .setActivatedByDefault(true)
                        .setMarkdownDescription(description);
                }
            }
        } catch (XMLStreamException e) {
            LOGGER.debug("PMD: Failed to load apex rules file");
            throw new IllegalStateException("XML is not valid", e);
        }
    }
}