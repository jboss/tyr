package org.xstefank.model.yaml;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class Format {

    private RegexDefinition title;
    private RegexDefinition commit;
    private SkipPatterns skipPatterns;
    private Description description;
    private List<String> additional;
    private Map<String, String> commands;

    @JsonProperty("CI")
    private List<String> CI;

    public SkipPatterns getSkipPatterns() {
        return skipPatterns;
    }

    public void setSkipPatterns(SkipPatterns skipPatterns) {
        this.skipPatterns = skipPatterns;
    }

    public RegexDefinition getTitle() {
        return title;
    }

    public void setTitle(RegexDefinition title) {
        this.title = title;
    }

    public RegexDefinition getCommit() {
        return commit;
    }

    public void setCommit(RegexDefinition commit) {
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

    public Map<String, String> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, String> commands) {
        this.commands = commands;
    }

    public List<String> getCI() {
        return CI;
    }

    public void setCI(List<String> CI) {
        this.CI = CI;
    }
}
