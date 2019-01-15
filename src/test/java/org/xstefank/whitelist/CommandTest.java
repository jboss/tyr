package org.xstefank.whitelist;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xstefank.TestUtils;
import org.xstefank.api.GitHubAPI;
import org.xstefank.ci.CILoader;
import org.xstefank.ci.TestCI;
import org.xstefank.model.Utils;

import static org.powermock.api.support.membermodification.MemberMatcher.method;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CILoader.class, GitHubAPI.class})
public abstract class CommandTest {

    public static final String PR_AUTHOR = "prUser";
    public static final String COMMENT_USER = "commentUser";

    static TestCI testCI;
    static File userListFile;
    private static File adminListFile;

    WhitelistProcessing whitelistProcessing;

    @BeforeClass
    public static void beforeClass() {
        System.setProperty(Utils.JBOSS_CONFIG_DIR, TestUtils.TARGET_DIR);
        userListFile = new File(System.getProperty(Utils.JBOSS_CONFIG_DIR), Utils.USERLIST_FILE_NAME);
        adminListFile = new File(System.getProperty(Utils.JBOSS_CONFIG_DIR), Utils.ADMINLIST_FILE_NAME);

        TestUtils.deleteFileIfExists(userListFile);
        TestUtils.deleteFileIfExists(adminListFile);
        TestUtils.writeUsernameToFile(COMMENT_USER, adminListFile);

        testCI = new TestCI();
    }

    @Before
    public void before() {
        // It is required to stub each method again for each invocation
        PowerMockito.stub(method(GitHubAPI.class, "getJsonWithPullRequest", JsonNode.class))
                .toReturn(TestUtils.TEST_PAYLOAD);

        whitelistProcessing = new WhitelistProcessing(TestUtils.FORMAT_CONFIG);
        testCI.init();
    }

    @After
    public void after() {
        TestUtils.deleteFileIfExists(userListFile);
        // Admin list is being reused
    }

    @AfterClass
    public static void afterClass() {
        TestUtils.deleteFileIfExists(adminListFile);
    }
}
