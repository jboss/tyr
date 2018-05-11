package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Logger;
import org.xstefank.api.GitHubAPI;
import org.xstefank.model.CommitStatus;
import org.xstefank.model.ConfigJSON;
import org.xstefank.model.Utils;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateChecker {

    private static final Logger log = Logger.getLogger(TemplateChecker.class);

    private static ConfigJSON config = readConfig();

    public static void checkPR(JsonNode payload) {
        StringBuilder status = new StringBuilder();

        if (config.getTitle() != null) {
            log.info("checking title");
            Pattern pattern = Pattern.compile(config.getTitle());
            Matcher matcher = pattern.matcher(payload.get(Utils.PULL_REQUEST).get(Utils.TITLE).asText());
            if (!matcher.matches()) {
                status.append(new TitleCheck(config.getTitle())).append(System.lineSeparator());
            }
        }


        log.info("updating status");
        String description = status.toString();

        GitHubAPI.updateCommitStatus("xstefank/test-repo",
                payload.get(Utils.PULL_REQUEST).get(Utils.HEAD).get(Utils.SHA).asText(),
                description.isEmpty() ? CommitStatus.SUCCESS : CommitStatus.ERROR,
                "https://github.com/xstefank/tyr/",
                description.isEmpty() ? "valid" : description, "PR format check");

    }

    private static ConfigJSON readConfig() {
        String configFileName = System.getProperty("template.format.file");
        log.info(configFileName);
        File configFile = new File(configFileName);
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(configFile, ConfigJSON.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot load configuration file", e);
        }
    }

}
