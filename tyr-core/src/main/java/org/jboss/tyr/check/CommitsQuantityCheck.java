/*
 * Copyright 2021 Red Hat, Inc, and individual contributors.
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
import org.jboss.tyr.InvalidPayloadException;
import org.jboss.tyr.github.GitHubService;
import org.jboss.tyr.model.yaml.CommitsQuantity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class CommitsQuantityCheck implements Check {

    static final String DEFAULT_MESSAGE = "Number of commits is not within the allowed scope";

    private final Pattern valueRegex = Pattern.compile("^([1-9]|[1-9][0-9]|100)$");
    private final Pattern rangeRegex = Pattern.compile("^([1-9]|[1-9][0-9]|100)-([1-9]|[1-9][0-9]|100)$");

    @Inject
    GitHubService gitHubService;

    private String message;

    private Integer parsedRangeBottomBoundary;
    private Integer parsedRangeUpperBoundary;

    public void initCheck(CommitsQuantity commitsQuantity) {
        if (commitsQuantity == null || commitsQuantity.getQuantity() == null) {
            throw new IllegalArgumentException("Quantity was not set");
        }

        String quantity = commitsQuantity.getQuantity();
        message = (commitsQuantity.getMessage() != null) ? commitsQuantity.getMessage() : DEFAULT_MESSAGE;

        Matcher valueMatcher = valueRegex.matcher(quantity);
        Matcher rangeMatcher = rangeRegex.matcher(quantity);

        if (valueMatcher.matches()) {
            parsedRangeBottomBoundary = Integer.parseInt(quantity);
        }
        else if (rangeMatcher.matches()) {
            parsedRangeBottomBoundary = Integer.parseInt(rangeMatcher.group(1));
            parsedRangeUpperBoundary = Integer.parseInt(rangeMatcher.group(2));

            if (parsedRangeBottomBoundary >= parsedRangeUpperBoundary) {
                throw new IllegalArgumentException("First value of range should be lower, than second one.");
            }
        }
        else {
            throw new IllegalArgumentException("Unrecognized commit range syntax. Use specific number, or range in format 1-20. Allowed upper boundary is 100.");
        }
    }

    @Override
    public String check(JsonObject payload) throws InvalidPayloadException {
        JsonArray commitsJsonArray = gitHubService.getCommitsJSON(payload);
        int numberOfCommits = commitsJsonArray.size();

        if (parsedRangeUpperBoundary == null) {
            return (numberOfCommits == parsedRangeBottomBoundary) ? null : message;
        } else {
            return (numberOfCommits >= parsedRangeBottomBoundary &&
                    numberOfCommits <= parsedRangeUpperBoundary) ? null : message;
        }
    }
}
