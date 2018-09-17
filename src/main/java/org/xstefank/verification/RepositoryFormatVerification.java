package org.xstefank.verification;

import org.xstefank.model.yaml.FormatConfig;

public class RepositoryFormatVerification implements Verification {

    @Override
    public void verify(FormatConfig formatConfig) throws InvalidConfigurationException {
        if (!formatConfig.getRepository().matches("^[a-zA-Z0-9_]*/[a-zA-Z0-9_-]*$"))
            throw new InvalidConfigurationException("Wrong repository format in configuration file");
    }
}