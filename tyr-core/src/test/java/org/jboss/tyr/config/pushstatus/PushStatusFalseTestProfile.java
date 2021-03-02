package org.jboss.tyr.config.pushstatus;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class PushStatusFalseTestProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of("tyr.github.status.push", "false");
    }
}
