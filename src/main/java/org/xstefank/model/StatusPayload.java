package org.xstefank.model;

public class StatusPayload {

    private String state;

    private String target_url;

    private String description;

    private String context;

    public StatusPayload() {
    }

    public StatusPayload(String state, String target_url, String description, String context) {
        this.state = state;
        this.target_url = target_url;
        this.description = description;
        this.context = context;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTarget_url() {
        return target_url;
    }

    public void setTarget_url(String target_url) {
        this.target_url = target_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "StatusPayload{" +
                "state='" + state + '\'' +
                ", target_url='" + target_url + '\'' +
                ", description='" + description + '\'' +
                ", context='" + context + '\'' +
                '}';
    }
}
