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
package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import org.xstefank.model.Utils;
import org.xstefank.model.yaml.FormatConfig;
import org.xstefank.model.yaml.RegexDefinition;

import java.util.regex.Matcher;

public class SkipCheck {

    public static boolean shouldSkip(JsonNode payload, FormatConfig config) {
        if (payload == null || config == null) {
            throw new IllegalArgumentException("Input arguments cannot be null!");
        }
        return skipByTitle(payload, config) || skipByCommit(payload, config) || skipByDescriptionFirstRow(payload, config);
    }

    private static boolean skipByTitle(JsonNode payload, FormatConfig config) {
        if (config.getFormat().getSkipPatterns().getTitle() != null) {
            Matcher titleMatcher = config.getFormat().getSkipPatterns().getTitle()
                    .matcher(payload.get(Utils.PULL_REQUEST).get(Utils.TITLE).asText());
            return titleMatcher.matches();
        }
        return false;
    }

    private static boolean skipByCommit(JsonNode payload, FormatConfig config) {
        if (config.getFormat().getSkipPatterns().getCommit() != null) {
            RegexDefinition commitRegexDefinition = new RegexDefinition();
            commitRegexDefinition.setPattern(config.getFormat().getSkipPatterns().getCommit());
            LatestCommitCheck latestCommitCheck = new LatestCommitCheck(commitRegexDefinition);
            return latestCommitCheck.check(payload) == null;
        }
        return false;
    }

    private static boolean skipByDescriptionFirstRow(JsonNode payload, FormatConfig config) {
        if (config.getFormat().getSkipPatterns().getDescription() != null) {
            String description = payload.get(Utils.PULL_REQUEST).get(Utils.BODY).asText();
            String firstRow = description.split(Utils.GITHUB_LINE_SEPARATOR, 2)[0];
            Matcher descriptionMatcher = config.getFormat().getSkipPatterns().getDescription().matcher(firstRow);
            return descriptionMatcher.matches();
        }
        return false;
    }
}
