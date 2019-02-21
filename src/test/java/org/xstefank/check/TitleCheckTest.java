package org.xstefank.check;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xstefank.TestUtils;
import org.xstefank.model.yaml.RegexDefinition;
import java.util.regex.Pattern;

public class TitleCheckTest {

    private RegexDefinition titleRegexDefinition;
    private TitleCheck titleCheck;

    @Before
    public void setUp() {
        titleRegexDefinition = new RegexDefinition();
    }

    @Test (expected=IllegalArgumentException.class)
    public void testNullTitleParameter() throws IllegalArgumentException {
        new TitleCheck(null);
    }

    @Test (expected=IllegalArgumentException.class)
    public void testNullCommitPatternParameter() throws IllegalArgumentException {
        titleRegexDefinition.setPattern(null);
        new TitleCheck(titleRegexDefinition);
    }

    @Test
    public void testCheckSimpleRegexMatch() {
        titleRegexDefinition.setPattern(Pattern.compile("^Test.*PR$"));
        titleCheck = new TitleCheck(titleRegexDefinition);

        Assert.assertNull("Cannot match valid regex", titleCheck.check(TestUtils.TEST_PAYLOAD));
    }

    @Test
    public void testCheckSimpleRegexNonMatchReturnsExpectedMessage() {
        titleRegexDefinition.setPattern(Pattern.compile("can't.*match.*this"));
        titleRegexDefinition.setMessage("This is titleRegexDefinition message");
        titleCheck = new TitleCheck(titleRegexDefinition);

        Assert.assertNotNull("Matched invalid regex", titleCheck.check(TestUtils.TEST_PAYLOAD));
        Assert.assertEquals("Unexpected message returned", titleRegexDefinition.getMessage(), titleCheck.check(TestUtils.TEST_PAYLOAD));
    }

    @Test
    public void testCheckSimpleRegexNonMatchReturnsDefaultMessage() {
        titleRegexDefinition.setPattern(Pattern.compile("can't.*match.*this"));
        titleCheck = new TitleCheck(titleRegexDefinition);

        Assert.assertEquals("Unexpected message returned", TitleCheck.DEFAULT_MESSAGE, titleCheck.check(TestUtils.TEST_PAYLOAD));
    }
}