package org.xstefank.check;

public class Violation {

    private String name;
    private String description;

    public Violation(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Violation{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
