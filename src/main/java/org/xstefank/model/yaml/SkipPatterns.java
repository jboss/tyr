package org.xstefank.model.yaml;

import java.util.regex.Pattern;

public class SkipPatterns {

    private Pattern title;
    private Pattern commit;
    private Pattern description;

    public Pattern getTitle() {
        return title;
    }

    public void setTitle(Pattern title) {
        this.title = title;
    }

    public Pattern getCommit() {
        return commit;
    }

    public void setCommit(Pattern commit) {
        this.commit = commit;
    }

    public Pattern getDescription() {
        return description;
    }

    public void setDescription(Pattern description) {
        this.description = description;
    }
}
