package org.jboss.tyr.api;

import org.junit.Test;

public class GitHubAPITest {

    @Test(expected = NullPointerException.class)
    public void testUpdateCommitStatusNullParameters() {
        GitHubAPI.updateCommitStatus(null, null, null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetJsonWithCommitsNullParameter() {
        GitHubAPI.getCommitsJSON(null);
    }
}
