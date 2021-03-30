/*
 * Copyright 2021 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.tyr.config;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
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
import java.util.Map;

import static org.mockito.ArgumentMatchers.isA;

@QuarkusTest
@TestProfile(PushStatusFalseTest.Profile.class)
public class PushStatusFalseTest {

    @Inject
    TemplateChecker templateChecker;

    @InjectMock
    GitHubService gitHubService;

    public static class Profile implements QuarkusTestProfile {
        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of("tyr.github.status.push", "false");
        }
    }

    @Test
    public void verifyThatGitHubIsNotCalledWhenPushStatusIsFalseTest() {
        Mockito.doThrow(new UnsupportedOperationException("This method should not have been called"))
            .when(gitHubService).updateCommitStatus(isA(String.class), isA(String.class),
            isA(CommitStatus.class), isA(String.class),
            isA(String.class), isA(String.class));

        Assertions.assertDoesNotThrow(() -> templateChecker.processPullRequest(TestUtils.TEST_PAYLOAD));
    }
}
