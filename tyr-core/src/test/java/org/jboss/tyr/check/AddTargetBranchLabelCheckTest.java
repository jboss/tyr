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
package org.jboss.tyr.check;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.jboss.tyr.InvalidPayloadException;
import org.jboss.tyr.TestUtils;
import org.jboss.tyr.github.GitHubService;
import org.jboss.tyr.model.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.inject.Inject;


@QuarkusTest
public class AddTargetBranchLabelCheckTest {

    @Inject
    AddTargetBranchLabelCheck addTargetBranchLabelCheck;

    @InjectSpy
    GitHubService gitHubService;

    @Test
    public void testColorLabel() throws IllegalArgumentException {
        //Valid color codes
        Assertions.assertTrue("a".repeat(6).matches(Utils.COLOR_VERIFICATION));
        Assertions.assertTrue("f".repeat(6).matches(Utils.COLOR_VERIFICATION));
        Assertions.assertTrue("0".repeat(6).matches(Utils.COLOR_VERIFICATION));
        Assertions.assertTrue("9".repeat(6).matches(Utils.COLOR_VERIFICATION));
        Assertions.assertTrue("af09f0".matches(Utils.COLOR_VERIFICATION));

        //Invalid color codes
        Assertions.assertFalse("a".repeat(7).matches(Utils.COLOR_VERIFICATION));
        Assertions.assertFalse("a".repeat(5).matches(Utils.COLOR_VERIFICATION));
    }

    @Test
    public void testAddLabel() throws InvalidPayloadException {
        ArgumentCaptor<String> targetBranchCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> pullRequestNumberCaptor = ArgumentCaptor.forClass(int.class);
        ArgumentCaptor<String> testRepositoryCaptor = ArgumentCaptor.forClass(String.class);

        String targetBranch =
                TestUtils.TEST_PAYLOAD.getJsonObject(Utils.PULL_REQUEST).getJsonObject(Utils.BASE).getString(Utils.REF);
        int pullRequestNumber = TestUtils.TEST_PAYLOAD.getInt(Utils.NUMBER);
        String testRepository = TestUtils.TEST_PAYLOAD.getJsonObject(Utils.REPOSITORY).getString(Utils.FULL_NAME);

        String addLabelCheckResult = addTargetBranchLabelCheck.check(TestUtils.TEST_PAYLOAD);
        Mockito.verify(gitHubService).addLabelToPullRequest
                (testRepositoryCaptor.capture(),
                        pullRequestNumberCaptor.capture(),
                        targetBranchCaptor.capture(),
                        Mockito.anyString(),
                        Mockito.anyString());

        Assertions.assertEquals(targetBranch, targetBranchCaptor.getValue());
        Assertions.assertEquals(pullRequestNumber, pullRequestNumberCaptor.getValue());
        Assertions.assertEquals(testRepository, testRepositoryCaptor.getValue());
        Assertions.assertNull(addLabelCheckResult, "Null expected");
    }
}
