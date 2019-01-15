package org.xstefank.whitelist;

import com.fasterxml.jackson.databind.JsonNode;

public interface Command {

    void process(JsonNode payload, WhitelistProcessing whitelistProcessing);

    String getCommandRegex();

    void setCommandRegex(String commandRegex);
}