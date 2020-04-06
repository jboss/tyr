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
package org.jboss.tyr.command;

import io.quarkus.test.junit.QuarkusTest;
import org.jboss.tyr.TestUtils;
import org.jboss.tyr.ci.TestCI;
import org.jboss.tyr.model.Utils;
import org.jboss.tyr.whitelist.WhitelistProcessing;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;
import java.io.File;

@QuarkusTest
public abstract class CommandTest {

    public static final String PR_AUTHOR = "prUser";
    public static final String COMMENT_USER = "commentUser";

    static File userListFile;
    private static File adminListFile;

    @Inject
    WhitelistProcessing whitelistProcessing;

    @Inject
    TestCI testCI;

    @BeforeAll
    public static void beforeAll() {
        userListFile = new File(TestUtils.TARGET_DIR, Utils.USERLIST_FILE_NAME);
        adminListFile = new File(TestUtils.TARGET_DIR, Utils.ADMINLIST_FILE_NAME);

        TestUtils.deleteFileIfExists(userListFile);
        TestUtils.deleteFileIfExists(adminListFile);
        TestUtils.writeLineToFile(COMMENT_USER, adminListFile);
    }

    @BeforeEach
    public void beforeEach() {
        whitelistProcessing.init(TestUtils.FORMAT_CONFIG_CI);
        testCI.init();
    }

    @AfterEach
    public void after() {
        TestUtils.deleteFileIfExists(userListFile);
        // Admin list is being reused
    }

    @AfterAll
    public static void afterClass() {
        TestUtils.deleteFileIfExists(adminListFile);
    }
}
