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
import java.util.ArrayList;
import java.util.List;

public class TemplateChecker {

    private static final Logger log = Logger.getLogger(TemplateChecker.class);

    private static List<Check> checks = loadChecks();

    public static void checkPR(JsonNode payload) {

        log.info("checking title");
        System.out.println(checks);

        log.info("updating status");
        GitHubAPI.updateCommitStatus("xstefank/test-repo",
                payload.get(Utils.PULL_REQUEST).get(Utils.HEAD).get(Utils.SHA).asText(),
                CommitStatus.SUCCESS, "https://github.com/xstefank/tyr/",
                "testing format", "PR format check");

    }

    private static List<Check> loadChecks() {
        checks = new ArrayList<>();
        ConfigJSON config;

        try {
            config = readConfig();
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot load configuration file", e);
        }

        if (config.getTitle() != null) {
            checks.add(new TitleCheck(config.getTitle()));
        }

        return checks;
    }

    private static ConfigJSON readConfig() throws IOException {
        String configFileName = System.getProperty("template.format.file");
        log.info(configFileName);
        File configFile = new File(configFileName);
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(configFile, ConfigJSON.class);
    }

}
