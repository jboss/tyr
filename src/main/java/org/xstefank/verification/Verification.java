package org.xstefank.verification;

import org.xstefank.model.yaml.FormatConfig;

public interface Verification {

    void verify(FormatConfig formatConfig) throws InvalidConfigurationException;
}