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
package org.xstefank.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xstefank.TestUtils;

public class PersistentListTest {

    private static final String FILE_NAME = "testList.txt";
    private static File userListFile = new File(TestUtils.TARGET_DIR, FILE_NAME);

    private final String USERNAME = "testUser";
    private PersistentList testUserList;

    @BeforeClass
    public static void beforeClass() {
        TestUtils.deleteFileIfExists(userListFile);
    }

    @Before
    public void before() {
        testUserList = new PersistentList(userListFile);
    }

    @Test
    public void testIfUserIsSaved() {
        testUserList.addUser(USERNAME);
        Assert.assertTrue(testUserList.hasUsername(USERNAME));
        Assert.assertTrue(TestUtils.fileContainsLine(userListFile, USERNAME));
    }

    @Test
    public void testIfFileWasCreated() {
        Assert.assertTrue(userListFile.exists());
    }

    @Test
    public void testFileContentLoading() throws IOException {
        FileWriter fw = new FileWriter(userListFile);
        fw.write(USERNAME);
        fw.close();

        testUserList = new PersistentList(userListFile);
        Assert.assertTrue(testUserList.hasUsername(USERNAME));
    }

    @After
    public void after() {
        TestUtils.deleteFileIfExists(userListFile);
    }
}
