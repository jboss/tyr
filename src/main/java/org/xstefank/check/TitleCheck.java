package org.xstefank.check;

public class TitleCheck extends Check {

    public TitleCheck(String regex) {
        super("Title", regex, "title does not match the expected format");
    }
}
