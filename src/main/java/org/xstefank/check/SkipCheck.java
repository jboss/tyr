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
            Matcher titleMatcher = config.getFormat().getSkipPatterns().getTitle().matcher(payload.get(Utils.PULL_REQUEST).get(Utils.TITLE).asText());
            return titleMatcher.matches();
        }
        return false;
    }

    private static boolean skipByCommit(JsonNode payload, FormatConfig config) {
        if (config.getFormat().getSkipPatterns().getCommit() != null) {
            RegexDefinition commitRegexDefinition = new RegexDefinition();
            commitRegexDefinition.setPattern(config.getFormat().getSkipPatterns().getCommit());
            LatestCommitCheck latestCommitCheck = new LatestCommitCheck(commitRegexDefinition);
            return (latestCommitCheck.check(payload) == null);
        }
        return false;
    }

    private static boolean skipByDescriptionFirstRow(JsonNode payload, FormatConfig config) {
        if (config.getFormat().getSkipPatterns().getDescription() != null) {
            String description = payload.get(Utils.PULL_REQUEST).get(Utils.BODY).asText();
            String firstRow = description.split("\\r\\n", 2)[0];
            Matcher descriptionMatcher = config.getFormat().getSkipPatterns().getDescription().matcher(firstRow);
            return descriptionMatcher.matches();
        }
        return false;
    }
}
