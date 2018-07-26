package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.jboss.logging.Logger;
import org.xstefank.api.GitHubAPI;
import org.xstefank.check.additional.AdditionalChecks;
import org.xstefank.model.CommitStatus;
import org.xstefank.model.yaml.FormatConfig;
import org.xstefank.model.yaml.Format;
import org.xstefank.model.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class TemplateChecker {

    public static final String TEMPLATE_FORMAT_FILE = "template.format.file";
    private static final Logger log = Logger.getLogger(TemplateChecker.class);

    private List<Check> checks;
    private FormatConfig config;

    public TemplateChecker() {
        config = readConfig();
        checks = registerChecks(config.getFormat());
    }

    public void checkPR(JsonNode payload) {
        String description = "";
        for (Check check : checks) {
            String message = check.check(payload);
            if (message != null) {
                description = message;
                break;
            }
        }

        log.info("updating status");

        GitHubAPI.updateCommitStatus(config.getRepository(),
                payload.get(Utils.PULL_REQUEST).get(Utils.HEAD).get(Utils.SHA).asText(),
                description.isEmpty() ? CommitStatus.SUCCESS : CommitStatus.ERROR,
                config.getStatusUrl(),
                description.isEmpty() ? "valid" : description, "PR format check");

    }

    private static List<Check> registerChecks(Format format) {
        List<Check> checks = new ArrayList<>();

        String title = format.getTitle();

        if (title != null) {
            checks.add(new TitleCheck(title));
        }

        if (format.getDescription() != null) {
            checks.add(new RequiredRowsCheck(format.getDescription().getRequiredRows()));
        }

        if (format.getCommit() != null) {
            checks.add(new LatestCommitCheck(format.getCommit()));
        } else if (title != null)
            checks.add(new LatestCommitCheck(title));

        for (String additional : format.getAdditional()) {
            checks.add(AdditionalChecks.findCheck(additional));
        }

        return checks;
    }

    private static FormatConfig readConfig() {
        String configFileName = System.getProperty(TEMPLATE_FORMAT_FILE);
        if (configFileName == null) {
            configFileName = System.getProperty(Utils.JBOSS_CONFIG_DIR) + "/format.yaml";
        }
        log.info(configFileName);
        File configFile = new File(configFileName);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            return mapper.readValue(configFile, FormatConfig.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot load configuration file", e);
        }
    }

}
