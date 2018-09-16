package org.xstefank.check.additional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xstefank.TestUtils;

import java.io.IOException;

public class TitleIssueLinkIncludedCheckTest {

    private TitleIssueLinkIncludedCheck titleIssueLinkIncludedCheck;

    @Before
    public void before() {
        titleIssueLinkIncludedCheck = new TitleIssueLinkIncludedCheck();
    }

    @Test
    public void testIfTitleHasRightFormat() {
        Assert.assertNull("Cannot match link to the issue", titleIssueLinkIncludedCheck.check(TestUtils.TEST_PAYLOAD));
    }

    @Test
    public void testIfTitleHasBadFormat() {
        Assert.assertNotNull("Matched invalid title issue link", titleIssueLinkIncludedCheck.check(TestUtils.BAD_TEST_PAYLOAD));
    }

    @Test(expected = NullPointerException.class)
    public void testIfNullParameterThrowsException() {
        titleIssueLinkIncludedCheck.check(null);
    }

    @Test(expected = NullPointerException.class)
    public void testIfEmptyPayloadThrowsException() throws IOException {
        titleIssueLinkIncludedCheck.check(TestUtils.EMPTY_PAYLOAD);
    }
}
