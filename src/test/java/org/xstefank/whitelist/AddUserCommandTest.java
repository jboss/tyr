package org.xstefank.whitelist;

import org.junit.Assert;
import org.junit.Test;
import org.xstefank.TestUtils;

public class AddUserCommandTest extends CommandTest {

    private AddUserCommand addUserCommand = new AddUserCommand();

    @Test
    public void testAddUserCommand() {
        addUserCommand.process(TestUtils.ISSUE_PAYLOAD, new WhitelistProcessing(TestUtils.FORMAT_CONFIG));
        Assert.assertTrue(TestUtils.fileContainsLine(userListFile, PR_AUTHOR));
        Assert.assertTrue(testCI.isTriggered());
    }

    @Test(expected = NullPointerException.class)
    public void testAddUserCommandNullParams() {
        addUserCommand.process(null, null);
    }
}
