package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import org.xstefank.api.GitHubAPI;
import org.xstefank.model.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LatestCommitCheck implements Check {

    private static final String ERROR_MESSAGE = "Invalid commit title format";

    private Pattern pattern;

    public LatestCommitCheck(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public String check(JsonNode payload) {
        JsonNode commitsJson = GitHubAPI.getJsonWithCommits(payload);
        Matcher matcher = pattern.matcher(commitsJson.get(commitsJson.size() - 1).get(Utils.COMMIT).get(Utils.MESSAGE).asText());

        if (!matcher.matches()) {
            return ERROR_MESSAGE;
        }

        return null;
    }
}
