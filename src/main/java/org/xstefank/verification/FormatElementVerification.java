package org.xstefank.verification;

import org.xstefank.model.yaml.FormatConfig;

public class FormatElementVerification implements Verification {

    @Override
    public void verify(FormatConfig formatConfig) throws InvalidConfigurationException {
        if (formatConfig.getFormat() == null)
            throw new InvalidConfigurationException("Element 'format' is not specified");
    }
}
