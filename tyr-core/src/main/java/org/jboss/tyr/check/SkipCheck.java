/*
 * Copyright 2019 Red Hat, Inc, and individual contributors.
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

import org.jboss.tyr.InvalidPayloadException;
import org.jboss.tyr.github.GitHubService;
import org.jboss.tyr.model.Utils;
import org.jboss.tyr.model.yaml.FormatYaml;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class SkipCheck {

    @Inject
    GitHubService gitHubService;

    public boolean shouldSkip(JsonObject payload, FormatYaml config) throws InvalidPayloadException {
        if (payload == null || config == null) {
            throw new IllegalArgumentException("Input arguments cannot be null");
        }
        return skipByTitle(payload, config) || skipByCommit(payload, config) || skipByDescriptionFirstRow(payload, config);
    }

    private boolean skipByTitle(JsonObject payload, FormatYaml config) {
        Pattern titlePattern = config.getFormat().getSkipPatterns().getTitle();
        if (titlePattern != null) {
            Matcher titleMatcher = titlePattern.matcher(payload.getJsonObject(Utils.PULL_REQUEST).getString(Utils.TITLE));
            return titleMatcher.matches();
        }
        return false;
    }


    private boolean skipByCommit(JsonObject payload, FormatYaml config) throws InvalidPayloadException {
        Pattern commitPattern = config.getFormat().getSkipPatterns().getCommit();
        if (commitPattern != null) {
            JsonArray commitsJsonArray = gitHubService.getCommitsJSON(payload);
            for (int i = 0; i < commitsJsonArray.size(); i++) {
                String commitMessage = commitsJsonArray.getJsonObject(i)
                        .getJsonObject(Utils.COMMIT)
                        .getString(Utils.MESSAGE);

                Matcher matcher = commitPattern.matcher(commitMessage.split(Utils.GITHUB_LINE_SEPARATOR, 2)[0]);

                if (!matcher.matches()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean skipByDescriptionFirstRow(JsonObject payload, FormatYaml config) {
        Pattern descriptionPattern = config.getFormat().getSkipPatterns().getDescription();
        if (descriptionPattern != null) {
            String description = payload.getJsonObject(Utils.PULL_REQUEST).getString(Utils.BODY);
            String firstRow = description.split(Utils.GITHUB_LINE_SEPARATOR, 2)[0];
            Matcher descriptionMatcher = descriptionPattern.matcher(firstRow);
            return descriptionMatcher.matches();
        }
        return false;
    }
}
