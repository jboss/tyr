package org.xstefank.model;

import java.util.List;

public class ConfigJSON {

    private String title;
    private List<String> description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }
}
