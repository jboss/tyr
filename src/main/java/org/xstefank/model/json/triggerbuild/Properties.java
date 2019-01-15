package org.xstefank.model.json.triggerbuild;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class Properties {

    @JsonProperty("property")
    private List<Property> properties;

    public Properties(String sha, String pull, String gitBranch) {
        properties = new ArrayList<>();
        properties.add(new Property("hash", sha));
        properties.add(new Property("pull", pull));
        properties.add(new Property("branch", gitBranch));
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
