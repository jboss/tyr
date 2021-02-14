package org.jboss.tyr.github;

import io.quarkus.test.junit.QuarkusTest;
import org.jboss.tyr.InvalidPayloadException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.inject.Named;

@QuarkusTest
public class GitHubServiceTest {

    @Inject @Named("default")
    GitHubService gitHubService;

    @Test
    public void testUpdateCommitStatusNullParameters() {
        Assertions.assertThrows(NullPointerException.class,
            () -> gitHubService.updateCommitStatus(null, null, null, null, null, null));
    }

    @Test
    public void testGetJsonWithCommitsNullParameter() {
        Assertions.assertThrows(InvalidPayloadException.class, () -> gitHubService.getCommitsJSON(null));
    }
}
