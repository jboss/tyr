package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.jboss.logging.Logger;
import org.xstefank.api.GitHubAPI;
import org.xstefank.model.CommitStatus;
import org.xstefank.model.FormatYAML;
import org.xstefank.model.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class TemplateChecker {

    private static final Logger log = Logger.getLogger(TemplateChecker.class);

    private List<Check> checks;
    private FormatYAML config;

    public TemplateChecker() {
        config = readConfig();
        checks = registerChecks(config);
    }

    public void checkPR(JsonNode payload) {
        StringJoiner joiner = new StringJoiner(", ");
        for (Check check : checks) {
            String message = check.check(payload);
            if (message != null) {
                joiner.add(message);
            }
        }

        log.info("updating status");
        String description = joiner.toString();

        GitHubAPI.updateCommitStatus(config.getRepository(),
                payload.get(Utils.PULL_REQUEST).get(Utils.HEAD).get(Utils.SHA).asText(),
                description.isEmpty() ? CommitStatus.SUCCESS : CommitStatus.ERROR,
                config.getUrl(),
                description.isEmpty() ? "valid" : description, "PR format check");

    }

    private static List<Check> registerChecks(FormatYAML config) {
        List<Check> checks = new ArrayList<>();

        if (config.getTitle() != null) {
            checks.add(new TitleCheck(config.getTitle()));
        }

        if (config.getDescription() != null) {
            checks.add(new RequiredRowsCheck(config.getDescription().getRequiredRows()));
        }

        return checks;
    }

    private static FormatYAML readConfig() {
        String configFileName = System.getProperty("template.format.file");
        log.info(configFileName);
        File configFile = new File(configFileName);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            return mapper.readValue(configFile, FormatYAML.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot load configuration file", e);
        }
    }

}
