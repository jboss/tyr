package org.xstefank.check;

import org.junit.Test;
import org.xstefank.model.yaml.FormatConfig;

public class TemplateCheckerTest {

    @Test(expected=IllegalArgumentException.class)
    public void testNullConfigParameter() throws IllegalArgumentException {
        new TemplateChecker(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNullFormatParameter() {
        FormatConfig testConfig = new FormatConfig();
        testConfig.setFormat(null);
        new TemplateChecker(testConfig);
    }
}