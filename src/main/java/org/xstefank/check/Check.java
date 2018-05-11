package org.xstefank.check;

public class Check {

    private String name;
    private String regex;
    private String description;

    public Check(String name, String regex, String description) {
        this.name = name;
        this.regex = regex;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getRegex() {
        return regex;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("%s: %s, expecting %s", name, description, regex);
    }
}
