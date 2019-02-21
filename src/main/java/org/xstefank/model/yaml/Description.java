package org.xstefank.model.yaml;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Description {

    @JsonProperty("required rows")
    private List<RegexDefinition> requiredRows;

    public List<RegexDefinition> getRequiredRows() {
        return new ArrayList<>(requiredRows);
    }

    public void setRequiredRows(List<RegexDefinition> requiredRows) {
        this.requiredRows = requiredRows;
    }
}
