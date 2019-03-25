package org.xstefank.ci;

import com.fasterxml.jackson.databind.JsonNode;

public interface ContinuousIntegration {

    void triggerBuild(JsonNode prPayload);

    void triggerFailedBuild(JsonNode prPayload);

    void init();
}