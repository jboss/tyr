package org.xstefank.whitelist;

import com.fasterxml.jackson.databind.JsonNode;
import org.xstefank.api.GitHubAPI;

public class RetestFailedCommand implements Command {

    public static final String NAME = "RetestFailedCommand";

    private String commandRegex;

    @Override
    public void process(JsonNode payload, WhitelistProcessing whitelistProcessing) {
        String pullRequestAuthor = whitelistProcessing.getPRAuthor(payload);
        String commentAuthor = whitelistProcessing.getCommentAuthor(payload);

        if (whitelistProcessing.isUserOnUserList(pullRequestAuthor) &&
                whitelistProcessing.isUserEligibleToRunCI(commentAuthor)) {
            JsonNode prPayload = GitHubAPI.getJsonWithPullRequest(payload);
            whitelistProcessing.triggerFailedCI(prPayload);
        }
    }

    @Override
    public String getCommandRegex() {
        return commandRegex;
    }

    @Override
    public void setCommandRegex(String commandRegex) {
        this.commandRegex = commandRegex;
    }
}
