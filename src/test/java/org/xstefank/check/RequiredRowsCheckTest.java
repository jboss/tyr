package org.xstefank.check;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xstefank.TestUtils;
import org.xstefank.model.yaml.RegexDefinition;

import java.io.IOException;
import java.util.regex.Pattern;

import static java.util.Collections.singletonList;

public class RequiredRowsCheckTest {

    private static RegexDefinition row;
    private RequiredRowsCheck requiredRowsCheck;

    @BeforeClass
    public static void beforeClass() {
        row = new RegexDefinition();
        row.setPattern(Pattern.compile("^Test.*description$"));
        row.setMessage("Does not match");
    }

    @Before
    public void before() {
        requiredRowsCheck = new RequiredRowsCheck(singletonList(row));
    }

    @Test
    public void checkSimpleRegexMatch() {
        Assert.assertNull("Cannot match valid description", requiredRowsCheck.check(TestUtils.TEST_PAYLOAD));
    }

    @Test
    public void checkSimpleRegexNonMatch() {
        row.setPattern(Pattern.compile("can't.*match.*this"));
        Assert.assertNotNull("Matched invalid description", requiredRowsCheck.check(TestUtils.TEST_PAYLOAD));
    }

    @Test(expected = NullPointerException.class)
    public void testIfNullParameterThrowsException() {
        RequiredRowsCheck requiredRowsCheck = new RequiredRowsCheck(null);
        requiredRowsCheck.check(TestUtils.TEST_PAYLOAD);
    }

    @Test(expected = NullPointerException.class)
    public void testIfEmptyPayloadThrowsException() throws IOException {
        requiredRowsCheck.check(TestUtils.EMPTY_PAYLOAD);
    }
}
