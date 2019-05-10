package org.jboss.tyr.check;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.tyr.TestUtils;
import org.jboss.tyr.check.resource.DummyAdditionalCheck;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

public class AdditionalChecksTest {

    private static final String ADDITIONAL_CHECKS_PROP = "additional-checks";

    @BeforeClass
    public static void beforeClass() {
        // create custom user jar
        JavaArchive customJar = ShrinkWrap.create(JavaArchive.class, "custom-checks.jar")
            .addClass(DummyAdditionalCheck.class)
            .addAsResource(new StringAsset("org.jboss.tyr.check.resource.DummyAdditionalCheck"),
                "META-INF/services/org.jboss.tyr.Check");

        customJar.as(ZipExporter.class).exportTo(new File("target/custom-checks.jar"));
    }

    @Test
    public void additionalChecksInvokedTest() {
        System.setProperty(ADDITIONAL_CHECKS_PROP, "target/custom-checks.jar");
        TemplateChecker templateChecker = new TemplateChecker(TestUtils.FORMAT_CONFIG);

        String result = templateChecker.checkPR(TestUtils.TEST_PAYLOAD);

        Assert.assertEquals("Additional check should have failed the validation",
            DummyAdditionalCheck.getMessage(), result);

        Assert.assertEquals("Additional check from custom jar should have been invoked",
            1, DummyAdditionalCheck.getCounterValue());
    }

    @Test
    public void invalidPathAdditionalCheckTest() {
        System.setProperty(ADDITIONAL_CHECKS_PROP, "target/invalid-path.jar");
        TemplateChecker templateChecker = new TemplateChecker(TestUtils.FORMAT_CONFIG);

        // should not fail, logs warning
        String result = templateChecker.checkPR(TestUtils.TEST_PAYLOAD);

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void emptyAdditionalChecksPropertyTest() {
        System.clearProperty(ADDITIONAL_CHECKS_PROP);
        TemplateChecker templateChecker = new TemplateChecker(TestUtils.FORMAT_CONFIG);

        String result = templateChecker.checkPR(TestUtils.TEST_PAYLOAD);

        Assert.assertTrue(result.isEmpty());
    }

    @AfterClass
    public static void afterClass() {
        TestUtils.deleteFileIfExists(new File("target/custom-checks.jar"));
    }
}
