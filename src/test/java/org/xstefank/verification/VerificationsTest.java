package org.xstefank.verification;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xstefank.model.yaml.FormatConfig;

import static org.xstefank.TestUtils.loadFormatFromYamlFile;
import static org.xstefank.TestUtils.YAML_DIR;
import static org.xstefank.TestUtils.FORMAT_CONFIG;

public class VerificationsTest {

    private static FormatConfig badFormatConfig;

    @BeforeClass
    public static void beforeClass() {
        badFormatConfig = loadFormatFromYamlFile(YAML_DIR + "/wrongTemplate.yaml");
    }

    @Test
    public void testReadValidFormatConfiguration() throws InvalidConfigurationException {
        VerificationHandler.verifyConfiguration(FORMAT_CONFIG);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testReadInvalidFormatConfiguration() throws InvalidConfigurationException {
        VerificationHandler.verifyConfiguration(badFormatConfig);
    }

    @Test(expected = NullPointerException.class)
    public void testNullParameterToVerify() throws InvalidConfigurationException {
        VerificationHandler.verifyConfiguration(null);
    }
}
