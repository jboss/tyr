package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import org.xstefank.model.Utils;
import org.xstefank.model.yaml.RegexDefinition;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleCheck implements Check {

    static final String DEFAULT_MESSAGE = "Invalid title content";

    private Pattern pattern;
    private String message;

    public TitleCheck(RegexDefinition title) {
        if (title == null || title.getPattern() == null) {
            throw new IllegalArgumentException("Input argument cannot be null!");
        }
        pattern = title.getPattern();
        message = (title.getMessage() != null) ? title.getMessage() : DEFAULT_MESSAGE;
    }

    @Override
    public String check(JsonNode payload) {
        Matcher matcher = pattern.matcher(payload.get(Utils.PULL_REQUEST).get(Utils.TITLE).asText());
        if (!matcher.matches()) {
            return message;
        }

        return null;
    }
}