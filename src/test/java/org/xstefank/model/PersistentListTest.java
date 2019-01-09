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