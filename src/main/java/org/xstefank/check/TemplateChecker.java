package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import org.jboss.logging.Logger;
import org.xstefank.api.GitHubAPI;
import org.xstefank.model.CommitStatus;
import org.xstefank.model.Utils;

public class TemplateChecker {

    private static final Logger log = Logger.getLogger(TemplateChecker.class);

    public static void checkPR(JsonNode payload) {

        log.info("updating status");
        GitHubAPI.updateCommitStatus("xstefank/test-repo",
                payload.get(Utils.PULL_REQUEST).get(Utils.HEAD).get(Utils.SHA).asText(),
                CommitStatus.SUCCESS, "https://github.com/xstefank/tyr/",
                "testing format", "PR format check");

    }

}
