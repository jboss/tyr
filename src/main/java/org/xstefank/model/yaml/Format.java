package org.xstefank.model.yaml;

import java.util.List;

public class Format {

    private String title;
    private String commit;
    private Description description;
    private List<String> additional;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommit() {
	return commit;
    }

    public void setCommit(String commit) {
	this.commit = commit;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public List<String> getAdditional() {
        return additional;
    }

    public void setAdditional(List<String> additional) {
        this.additional = additional;
    }
}
