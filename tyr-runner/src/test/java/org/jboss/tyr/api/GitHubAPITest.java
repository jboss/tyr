package org.jboss.tyr.api;

import org.jboss.tyr.InvalidPayloadException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GitHubAPITest {

    @Test
    public void testUpdateCommitStatusNullParameters() {
        Assertions.assertThrows(NullPointerException.class,
            () -> GitHubAPI.updateCommitStatus(null, null, null, null, null, null));
    }

    @Test
    public void testGetJsonWithCommitsNullParameter() throws InvalidPayloadException {
        Assertions.assertThrows(InvalidPayloadException.class, () -> GitHubAPI.getCommitsJSON(null));
    }
}
