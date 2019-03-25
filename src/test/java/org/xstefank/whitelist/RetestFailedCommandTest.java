package org.xstefank.whitelist;

import org.junit.Assert;
import org.junit.Test;
import org.xstefank.TestUtils;

public class RetestFailedCommandTest extends CommandTest {

    private RetestFailedCommand retestFailedCommand = new RetestFailedCommand();

    @Test
    public void testIfRetestFailedCommandTriggersCI() {
        whitelistProcessing.addUserToUserList(PR_AUTHOR);
        retestFailedCommand.process(TestUtils.ISSUE_PAYLOAD, whitelistProcessing);
        Assert.assertTrue(testCI.isTriggeredFailed());
    }

    @Test(expected = NullPointerException.class)
    public void testRetestFailedCommandNullParams() {
        retestFailedCommand.process(null, null);
    }
}
