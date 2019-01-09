package org.xstefank.whitelist;

import org.junit.Assert;
import org.junit.Test;
import org.xstefank.TestUtils;

public class RetestCommandTest extends CommandTest {

    private RetestCommand retestCommand = new RetestCommand();

    @Test
    public void testIfRetestCommandTriggersCI() {
        whitelistProcessing.addUserToUserList(PR_AUTHOR);
        retestCommand.process(TestUtils.ISSUE_PAYLOAD, whitelistProcessing);
        Assert.assertTrue(testCI.isTriggered());
    }

    @Test(expected = NullPointerException.class)
    public void testRetestCommandNullParams() {
        retestCommand.process(null, null);
    }
}
