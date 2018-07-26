package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import org.jboss.logging.Logger;
import org.xstefank.api.GitHubAPI;
import org.xstefank.model.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LatestCommitCheck implements Check {


    private static final Logger log = Logger.getLogger(LatestCommitCheck.class);
    private static final String ERROR_MESSAGE = "Invalid commit title format";

    private Pattern pattern;

    public LatestCommitCheck(String regex) {
        this.pattern = Pattern.compile(regex);
        log.info(pattern.toString());
    }

    @Override
    public String check(JsonNode payload) {
        log.info("checking commit title");
        JsonNode commitsJson = GitHubAPI.getJsonWithCommits(payload);
        Matcher matcher = pattern.matcher(commitsJson.get(commitsJson.size() - 1).get(Utils.COMMIT).get(Utils.MESSAGE).asText());

        if (!matcher.matches()) {
            return ERROR_MESSAGE;
        }

        return null;
    }
}
