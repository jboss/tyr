package org.xstefank.model;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xstefank.TestUtils;
import org.xstefank.check.TemplateChecker;

public class ConfigTest {

    @BeforeClass
    public static void beforeClass() {
        Utils.loadProperties("", TestUtils.TEST_CONFIG_PATH);
    }

    @Test
    public void testValidTemplateConfig() {
        TemplateChecker templateChecker = new TemplateChecker(TestUtils.FOMAT_CONFIG);
        Assert.assertTrue(templateChecker.checkPR(TestUtils.TEST_PAYLOAD).isEmpty());
    }

    @Test
    public void readTokenTest() {
        Assert.assertEquals("351wa351d38aw4c97w98f7987ew98f987we97gs4",
                Utils.getTyrProperty(Utils.TOKEN_PROPERTY));
    }

    @Test
    public void readUrlTest() {
        Assert.assertEquals("https://www.someniceurlhere.org/",
                Utils.getTyrProperty(Utils.TEMPLATE_FORMAT_URL));
    }
}