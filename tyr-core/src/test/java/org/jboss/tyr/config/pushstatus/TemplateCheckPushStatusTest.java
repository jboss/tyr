package org.jboss.tyr.config.pushstatus;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.junit.mockito.InjectMock;
import org.jboss.tyr.TestUtils;
import org.jboss.tyr.check.TemplateChecker;
import org.jboss.tyr.github.GitHubService;
import org.jboss.tyr.model.CommitStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;

import static org.mockito.ArgumentMatchers.isA;

@QuarkusTest
@TestProfile(PushStatusFalseTestProfile.class)
public class TemplateCheckPushStatusTest {

    @Inject
    TemplateChecker templateChecker;

    @InjectMock
    GitHubService gitHubService;

    @Test
    public void verifyThatGitHubIsNotCalledWhenPushStatusIsFalseTest() {
        Mockito.doThrow(new UnsupportedOperationException("This method should not have been called"))
            .when(gitHubService).updateCommitStatus(isA(String.class), isA(String.class),
            isA(CommitStatus.class), isA(String.class),
            isA(String.class), isA(String.class));

        Assertions.assertDoesNotThrow(() -> templateChecker.processPullRequest(TestUtils.TEST_PAYLOAD));
    }

}
