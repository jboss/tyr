package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;

public interface Check {

    String check(JsonNode payload);
}
