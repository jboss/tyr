package org.xstefank.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FormatYAML {

    private String repository;
    private String title;
    private Description description;

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public static class Description {

        @JsonProperty("required rows")
        private List<Row> requiredRows;

        public List<Row> getRequiredRows() {
            return new ArrayList<>(requiredRows);
        }

        public void setRequiredRows(List<Row> requiredRows) {
            this.requiredRows = requiredRows;
        }
    }

    public static class Row {
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
}
