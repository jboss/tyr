package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class TitleCheckTest {

    private TitleCheck titleCheck;
    private static JsonNode testPayload;
    
    @BeforeClass
    public static void beforeClass() throws IOException {
        File testFile = new File(TitleCheckTest.class.getClassLoader().getResource("testPayload.json").getFile());
        testPayload = new ObjectMapper().readTree(testFile);
    }

    @Test
    public void checkSimpleRegexMatch() {
        titleCheck = new TitleCheck(Pattern.compile("^Test.*PR$"));

        Assert.assertNull("Cannot match valid regex", titleCheck.check(testPayload));
    }

    @Test
    public void checkSimpleRegexNonMatch() {
        titleCheck = new TitleCheck(Pattern.compile("can't.*match.*this"));

        Assert.assertNotNull("Matched invalid regex", titleCheck.check(testPayload));
    }
}
