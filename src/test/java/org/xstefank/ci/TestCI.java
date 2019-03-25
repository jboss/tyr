package org.xstefank.ci;

import com.fasterxml.jackson.databind.JsonNode;

public class TestCI implements ContinuousIntegration {

    public static final String NAME = "TestCI";
    private boolean triggered;
    private boolean triggeredFailed;

    public TestCI() {
        CILoader.addCI(NAME, this);
    }

    @Override
    public void triggerBuild(JsonNode prPayload) {
        triggered = true;
    }

    @Override
    public void triggerFailedBuild(JsonNode prPayload) {
        triggeredFailed = true;
    }

    @Override
    public void init() {
        triggered = false;
        triggeredFailed = false;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public boolean isTriggeredFailed() {
        return triggeredFailed;
    }
}
