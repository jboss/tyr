package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import org.xstefank.api.GitHubAPI;
import org.xstefank.model.Utils;
import org.xstefank.model.yaml.RegexDefinition;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LatestCommitCheck implements Check {

    static final String DEFAULT_MESSAGE = "Invalid commit title content";

    private Pattern pattern;
    private String message;

    public LatestCommitCheck(RegexDefinition commit) {
        if (commit == null || commit.getPattern() == null) {
            throw new IllegalArgumentException("Input argument cannot be null!");
        }
        this.pattern = commit.getPattern();
        this.message = (commit.getMessage() != null) ? commit.getMessage() : DEFAULT_MESSAGE;
    }

    @Override
    public String check(JsonNode payload) {
        JsonNode commitsJson = GitHubAPI.getJsonWithCommits(payload);
        Matcher matcher = pattern.matcher(commitsJson.get(commitsJson.size() - 1).get(Utils.COMMIT).get(Utils.MESSAGE).asText());

        if (!matcher.matches()) {
            return message;
        }

        return null;
    }
}