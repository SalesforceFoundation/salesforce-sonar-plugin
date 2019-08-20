# salesforce-sonar-plugin

This plugin reads [PMD](https://pmd.github.io/) scan results for Apex code and adds the results to [SonarQube](https://sonarqube.org).

## Installation

Compile the plugin using `mvn install`. Copy the resulting sonar-salesforce-pmd-1.x.x-SNAPSHOT.jar file to your SonarQube plugins directory.

## Usage

Run a PMD scan against your Apex files, using the quickstart ruleset. Save the results to a file in your working directory.

`${pmdPath}/bin/run.sh pmd -dir src/classes/ -f xml -language apex -R rulesets/apex/quickstart.xml -cache pmdcache -failOnViolation false> pmd-apexunit.xml`


In your SonarQube instance, go to Administration > Configuration > Salesforce-PMD and set the path name of your results file. In this case, ${WORKSPACE}/pmd-report.xml.