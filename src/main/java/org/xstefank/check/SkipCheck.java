package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import org.xstefank.model.Utils;
import org.xstefank.model.yaml.FormatConfig;

import java.util.regex.Matcher;

public class SkipCheck {
    
    public static boolean shouldSkip(JsonNode payload, FormatConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Argument config is null!");
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
            LatestCommitCheck latestCommitCheck = new LatestCommitCheck(config.getFormat().getSkipPatterns().getCommit());
            return (latestCommitCheck.check(payload) == null);
        }
        return false;
    }

    private static boolean skipByDescriptionFirstRow(JsonNode payload, FormatConfig config) {
        if (config.getFormat().getSkipPatterns().getDescription() != null) {
            String description = payload.get(Utils.PULL_REQUEST).get(Utils.BODY).asText();
            String firstRow = description.split(System.lineSeparator(),2)[0];
            Matcher descriptionMatcher = config.getFormat().getSkipPatterns().getDescription().matcher(firstRow);
            return descriptionMatcher.matches();
        }
        return false;
    }
}
