package org.xstefank;

import org.junit.Assert;
import org.junit.Test;
import org.xstefank.check.TemplateChecker;
import org.xstefank.model.Utils;

public class YamlConfigTest {

    @Test
    public void testValidTemplateConfig() {
        TemplateChecker templateChecker = new TemplateChecker(TestUtils.FOMAT_CONFIG);
        Assert.assertTrue(templateChecker.checkPR(TestUtils.TEST_PAYLOAD).isEmpty());
    }

    @Test
    public void readTokenTest() {
        String pathToTestConfig = YamlConfigTest.class.getClassLoader().getResource("testConfig.properties").getPath();
        Assert.assertEquals("351wa351d38aw4c97w98f7987ew98f987we97gs4",
                Utils.readTokenFromProperties("", pathToTestConfig));
    }
}