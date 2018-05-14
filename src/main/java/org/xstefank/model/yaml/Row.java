package org.xstefank.model.yaml;

import java.util.regex.Pattern;

public class Row {

    private Pattern pattern;
    private String message;

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
