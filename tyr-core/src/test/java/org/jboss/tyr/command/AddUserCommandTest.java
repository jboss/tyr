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
package org.jboss.tyr.command;

import io.quarkus.test.junit.QuarkusTest;
import org.jboss.tyr.InvalidPayloadException;
import org.jboss.tyr.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class AddUserCommandTest extends CommandTest {

    @Inject
    AddUserCommand addUserCommand;

    @Test
    public void testAddUserCommand() throws InvalidPayloadException {
        addUserCommand.process(TestUtils.ISSUE_PAYLOAD, whitelistProcessing);
        Assertions.assertTrue(TestUtils.fileContainsLine(userListFile, PR_AUTHOR));
        Assertions.assertTrue(testCI.isTriggered());
    }

    @Test
    public void testAddUserCommandNullParams() {
        Assertions.assertThrows(NullPointerException.class, () -> addUserCommand.process(null, null));
    }
}
