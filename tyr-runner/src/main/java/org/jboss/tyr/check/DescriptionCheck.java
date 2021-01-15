/*
 * Copyright 2019-2021 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.tyr.check;

import org.jboss.tyr.Check;
import org.jboss.tyr.model.Utils;
import org.jboss.tyr.model.yaml.OptionalPattern;
import org.jboss.tyr.model.yaml.RegexDefinition;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

public class DescriptionCheck implements Check {

    private List<RegexDefinition> rows;
    private List<OptionalPattern> optionalPatterns;

    public DescriptionCheck(List<RegexDefinition> rows, List<OptionalPattern> optionalPatterns) {
        this.rows = rows;
        this.optionalPatterns = optionalPatterns;
    }

    @Override
    public String check(JsonObject payload) {
        List<RegexDefinition> requiredRows = new ArrayList<>(rows);
        List<OptionalPattern> optionalRows = new ArrayList<>(optionalPatterns);
        String description = payload.getJsonObject(Utils.PULL_REQUEST).getString(Utils.BODY);

        try (Scanner scanner = new Scanner(description)) {
            while (scanner.hasNextLine() && (!requiredRows.isEmpty() || !optionalRows.isEmpty())) {
                String line = scanner.nextLine();
                for (RegexDefinition row : requiredRows) {
                    Matcher matcher = row.getPattern().matcher(line);
                    if (matcher.matches()) {
                        requiredRows.remove(row);
                        break;
                    }
                }
                for (OptionalPattern optionalRow : optionalRows){
                    Matcher preconditionMatcher = optionalRow.getPrecondition().matcher(line);
                    if (preconditionMatcher.matches()){
                        Matcher patternMatcher = optionalRow.getPattern().matcher(line);
                        if (!patternMatcher.matches()){
                            return optionalRow.getMessage();
                        }
                        else {
                            optionalRows.remove(optionalRow);
                            break;
                        }
                    }
                }
            }
        }

        if (!requiredRows.isEmpty()) {
            return requiredRows.get(0).getMessage();
        }

        return null;
    }
}
