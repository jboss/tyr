package org.xstefank.check.additional;

import com.fasterxml.jackson.databind.JsonNode;
import org.xstefank.check.Check;
import org.xstefank.model.Utils;

public class OneCommitOnlyCheck implements Check {

    @Override
    public String check(JsonNode payload) {
        int numCommits = payload.get(Utils.PULL_REQUEST).get(Utils.COMMITS).asInt();

        if (numCommits > 1) {
            return "Please rebase the PR to only one commit";
        }

        return null;
    }
}
