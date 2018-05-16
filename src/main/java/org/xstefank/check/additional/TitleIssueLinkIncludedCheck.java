package org.xstefank.check.additional;

import com.fasterxml.jackson.databind.JsonNode;
import org.xstefank.check.Check;
import org.xstefank.model.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleIssueLinkIncludedCheck implements Check {

    private static final String JIRA_PREFIX = "https://issues.jboss.org/browse/";
    private static Pattern titlePatter = Pattern.compile("WFLY-\\d+");

    @Override
    public String check(JsonNode payload) {
        //expecting title in format [WFLY-XYZ] subject or WFLY-XYZ subject
        Matcher matcher = titlePatter.matcher(payload.get(Utils.PULL_REQUEST).get(Utils.TITLE).asText());
        if (matcher.find()) {
            String titleIssue = matcher.group();
            String description = payload.get(Utils.PULL_REQUEST).get(Utils.BODY).asText();

            if (!description.contains(JIRA_PREFIX + titleIssue)) {
                return "The description does not contain the link to issue in the PR title";
            }
        }

        return null;
    }
}
