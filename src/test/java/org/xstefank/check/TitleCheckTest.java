package org.xstefank.check;

import org.junit.Assert;
import org.junit.Test;
import org.xstefank.TestUtils;

import java.util.regex.Pattern;

public class TitleCheckTest {

    private TitleCheck titleCheck;

    @Test
    public void checkSimpleRegexMatch() {
        titleCheck = new TitleCheck(Pattern.compile("^Test.*PR$"));
        Assert.assertNull("Cannot match valid regex", titleCheck.check(TestUtils.TEST_PAYLOAD));
    }

    @Test
    public void checkSimpleRegexNonMatch() {
        titleCheck = new TitleCheck(Pattern.compile("can't.*match.*this"));
        Assert.assertNotNull("Matched invalid regex", titleCheck.check(TestUtils.TEST_PAYLOAD));
    }
}
