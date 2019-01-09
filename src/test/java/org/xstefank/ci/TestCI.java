package org.xstefank.ci;

import com.fasterxml.jackson.databind.JsonNode;

public class TestCI implements ContinuousIntegration {

    public static final String NAME = "TestCI";
    private boolean triggered;

    public TestCI() {
        CILoader.addCI(NAME, this);
    }

    @Override
    public void triggerBuild(JsonNode prPayload) {
        triggered = true;
    }

    @Override
    public void init() {
        triggered = false;
    }

    public boolean isTriggered() {
        return triggered;
    }
}
