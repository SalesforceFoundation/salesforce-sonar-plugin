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
package org.sonar.salesforce.parser.element;

// import org.sonar.api.batch.fs.TextPointer;
// import org.sonar.api.batch.fs.TextRange;

public class Violation {

    private String rule;
    private String description;
    private String ruleset;
    private String externalInfoUrl;
    private String priority;
    private String beginLine;
    private String endLine;
    private String beginColumn;
    private String endColumn;
    // private TextPointer start;
    // private TextPointer end;

    public String getRule() {
        return rule;
    }

    public void setRule(String rule){
        this.rule = rule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getRuleset() {
        return ruleset;
    }

    public void setRuleset(String ruleset){
        this.ruleset = ruleset;
    }

    public String getExternalInfoUrl() {
        return externalInfoUrl;
    }

    public void setExternalInfoUrl(String externalInfoUrl){
        this.externalInfoUrl = externalInfoUrl;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority){
        this.priority = priority;
    }

    public String getBeginLine() {
        return beginLine;
    }

    public void setBeginLine(String beginLine){
        this.beginLine = beginLine;
    }

    public String getEndLine() {
        return endLine;
    }

    public void setEndLine(String endLine){
        this.endLine = endLine;
    }

    public String getBeginColumn() {
        return beginColumn;
    }

    public void setBeginColumn(String beginColumn){
        this.beginColumn = beginColumn;
    }

    public String getEndColumn() {
        return endColumn;
    }

    public void setEndColumn(String endColumn){
        this.endColumn = endColumn;
    }

    // public TextPointer getStartLocation() {
    //     TextPointer start = new TextPointer(Integer.parseInt(this.beginLine), Integer.parseInt(this.beginColumn));
    //     return start;
    // }

    // public TextPointer getEndLocation() {
    //     return new TextPointer(Integer.parseInt(this.endLine), Integer.parseInt(this.endColumn));
    // }

    // public TextRange getRange() {
    //     return new TextRange(this.getStartLocation(), this.getEndLocation());
    // }

}
