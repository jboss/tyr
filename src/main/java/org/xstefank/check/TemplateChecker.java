package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import org.jboss.logging.Logger;
import org.xstefank.api.GitHubAPI;
import org.xstefank.check.additional.AdditionalChecks;
import org.xstefank.model.CommitStatus;
import org.xstefank.model.yaml.FormatConfig;
import org.xstefank.model.yaml.Format;
import org.xstefank.model.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TemplateChecker {

    public static final String TEMPLATE_FORMAT_FILE = "template.format.file";
    private static final Logger log = Logger.getLogger(TemplateChecker.class);

    private List<Check> checks;
    private FormatConfig config;

    public TemplateChecker(FormatConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Argument config cannot be null");
        }
        this.config = config;
        checks = registerChecks(config.getFormat());
    }

    public void checkPR(JsonNode payload) {
        log.info("checking PR");
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

        if (format.getTitle() != null) {
            checks.add(new TitleCheck(format.getTitle()));
        }

        if (format.getDescription() != null) {
            checks.add(new RequiredRowsCheck(format.getDescription().getRequiredRows()));
        }

        if (format.getCommit() != null) {
            checks.add(new LatestCommitCheck(format.getCommit()));
        }

        for (String additional : format.getAdditional()) {
            checks.add(AdditionalChecks.findCheck(additional));
        }

        return checks;
    }
}
