package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import org.jboss.logging.Logger;
import org.xstefank.api.GitHubAPI;
import org.xstefank.model.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LatestCommitCheck implements Check {


    private static final Logger log = Logger.getLogger(TitleCheck.class);
    private static final String ERROR_MESSAGE = "Invalid commit title format";

    private Pattern pattern;

    public LatestCommitCheck(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public String check(JsonNode payload) {
        log.info("checking commit title");
        JsonNode newJson = GitHubAPI.getJsonWithCommits(payload);
        Matcher matcher = pattern.matcher(newJson.get(newJson.size() - 1).get(Utils.COMMIT).get(Utils.MESSAGE).asText());

        if (!matcher.matches()) {
            return ERROR_MESSAGE;
        }

        return null;
    }
}
