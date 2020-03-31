/*
 * Copyright 2019 Red Hat, Inc, and individual contributors.
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

import org.jboss.tyr.InvalidPayloadException;
import org.jboss.tyr.TestUtils;
import org.jboss.tyr.model.yaml.RegexDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

public class CommitMessagesCheckTest {

    private RegexDefinition commitRegexDefinition;
    private CommitMessagesCheck commitMessagesCheck;

    @BeforeEach
    public void before() {
        commitRegexDefinition = new RegexDefinition();
//        PowerMockito.suppress(method(GitHubAPI.class, TestUtils.READ_TOKEN));
//        PowerMockito.stub(method(GitHubAPI.class, TestUtils.GET_JSON_WITH_COMMITS, JsonObject.class)).toReturn(TestUtils.TEST_COMMITS_PAYLOAD);
    }

    @Test
    public void testNullCommitParameter() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new CommitMessagesCheck(null));
    }

    @Test
    public void testNullCommitPatternParameter() {
        commitRegexDefinition.setPattern(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> new CommitMessagesCheck(commitRegexDefinition));
    }

    @Test
    @Disabled("powermock")
    public void testCheckSimpleRegexMatch() throws InvalidPayloadException {
        commitRegexDefinition.setPattern(Pattern.compile("Test commit"));
        commitMessagesCheck = new CommitMessagesCheck(commitRegexDefinition);

        Assertions.assertNull(commitMessagesCheck.check(TestUtils.TEST_PAYLOAD), "Cannot match valid regex");
    }

    @Test
    @Disabled("powermock")
    public void testCheckSimpleRegexNonMatchReturnsExpectedMessage() throws InvalidPayloadException {
        commitRegexDefinition.setPattern(Pattern.compile("can't.*match.*this"));
        commitRegexDefinition.setMessage("This is commitRegexDefinition message.");
        commitMessagesCheck = new CommitMessagesCheck(commitRegexDefinition);
        String result = commitMessagesCheck.check(TestUtils.TEST_PAYLOAD);

        Assertions.assertNotNull(result, "Matched invalid regex");
        Assertions.assertEquals("This is commitRegexDefinition message.", result, "Unexpected message returned");
    }

    @Test
    @Disabled("powermock")
    public void testCheckSimpleRegexNonMatchReturnsDefaultMessage() throws InvalidPayloadException {
        commitRegexDefinition.setPattern(Pattern.compile("can't.*match.*this"));
        commitMessagesCheck = new CommitMessagesCheck(commitRegexDefinition);

        Assertions.assertEquals(CommitMessagesCheck.DEFAULT_MESSAGE, commitMessagesCheck.check(TestUtils.TEST_PAYLOAD), "Unexpected message returned");
    }

    @Test
    @Disabled("powermock")
    public void testMultipleCommitMessages() throws InvalidPayloadException {
//        PowerMockito.stub(method(GitHubAPI.class, TestUtils.GET_JSON_WITH_COMMITS, JsonObject.class)).toReturn(TestUtils.MULTIPLE_COMMIT_MESSAGES_PAYLOAD);

        commitRegexDefinition.setPattern(Pattern.compile("Test commit"));
        commitMessagesCheck = new CommitMessagesCheck(commitRegexDefinition);

        Assertions.assertNull(commitMessagesCheck.check(TestUtils.TEST_PAYLOAD));
    }
}
