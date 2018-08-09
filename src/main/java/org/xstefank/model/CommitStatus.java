package org.xstefank.model;

public enum CommitStatus {

    ERROR("error"),
    SUCCESS("success"),
    PENDING("pending")
    ;

    private String value;

    CommitStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
