package org.xstefank.verification;

import org.xstefank.model.yaml.FormatConfig;

import java.util.ArrayList;
import java.util.List;

public class VerificationHandler {

    private static final List<Verification> verifications = registerVerifications();

    public static void verifyConfiguration(FormatConfig formatConfig) throws InvalidConfigurationException {
        for (Verification verification : verifications) {
            verification.verify(formatConfig);
        }
    }

    private static List<Verification> registerVerifications() {
        List<Verification> verifications = new ArrayList<>();

        verifications.add(new RepositoryFormatVerification());
        verifications.add(new FormatElementVerification());

        return verifications;
    }
}