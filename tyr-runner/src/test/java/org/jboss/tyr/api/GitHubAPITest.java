package org.jboss.tyr.api;

import org.jboss.tyr.InvalidPayloadException;
import org.junit.Test;

public class GitHubAPITest {

    @Test(expected = NullPointerException.class)
    public void testUpdateCommitStatusNullParameters() {
        GitHubAPI.updateCommitStatus(null, null, null, null, null, null);
    }

    @Test(expected = InvalidPayloadException.class)
    public void testGetJsonWithCommitsNullParameter() throws InvalidPayloadException {
        GitHubAPI.getCommitsJSON(null);
    }
}
