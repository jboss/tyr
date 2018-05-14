package org.xstefank.model.yaml;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Description {

    @JsonProperty("required rows")
    private List<Row> requiredRows;

    public List<Row> getRequiredRows() {
        return new ArrayList<>(requiredRows);
    }

    public void setRequiredRows(List<Row> requiredRows) {
        this.requiredRows = requiredRows;
    }
}
