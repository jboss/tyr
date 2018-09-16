package org.xstefank.check.additional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xstefank.TestUtils;

public class OneCommitOnlyCheckTest {

    private OneCommitOnlyCheck oneCommitOnlyCheck;

    @Before
    public void before() {
        oneCommitOnlyCheck = new OneCommitOnlyCheck();
    }

    @Test
    public void testIfPayloadHasOnlyOneCommit() {
        Assert.assertNull("Invalid commits count", oneCommitOnlyCheck.check(TestUtils.TEST_PAYLOAD));
    }

    @Test
    public void testIfPayloadHasMoreCommits() {
        Assert.assertNotNull("Commits count should not be valid", oneCommitOnlyCheck.check(TestUtils.BAD_TEST_PAYLOAD));
    }

    @Test(expected = NullPointerException.class)
    public void testNullParameterAsPayload() {
        oneCommitOnlyCheck.check(null);
    }

    @Test(expected = NullPointerException.class)
    public void testCheckEmptyPayload() {
        oneCommitOnlyCheck.check(TestUtils.EMPTY_PAYLOAD);
    }
}