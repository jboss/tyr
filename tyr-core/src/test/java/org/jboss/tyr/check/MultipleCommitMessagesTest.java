/*
 * Copyright 2019-2021 Red Hat, Inc, and individual contributors.
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
package org.jboss.tyr.check;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.jboss.tyr.InvalidPayloadException;
import org.jboss.tyr.TestUtils;
import org.jboss.tyr.github.GitHubService;
import org.jboss.tyr.model.yaml.RegexDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.json.JsonObject;
import java.util.regex.Pattern;

import static org.mockito.ArgumentMatchers.isA;

@QuarkusTest
public class MultipleCommitMessagesTest {

    @Inject
    CommitMessagesCheck commitMessagesCheck;

    @InjectMock
    GitHubService gitHubService;

    @Test
    public void testMultipleCommitMessages() throws InvalidPayloadException {
        Mockito.when(gitHubService.getCommitsJSON(isA(JsonObject.class))).thenReturn(TestUtils.MULTIPLE_COMMIT_MESSAGES_PAYLOAD);

        RegexDefinition commitRegexDefinition = new RegexDefinition();
        commitRegexDefinition.setPattern(Pattern.compile("Test commit"));
        commitMessagesCheck.setRegex(commitRegexDefinition);

        Assertions.assertNull(commitMessagesCheck.check(TestUtils.TEST_PAYLOAD), "Null expected");
    }
}

