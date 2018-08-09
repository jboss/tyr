package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import org.xstefank.model.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleCheck implements Check {

    private static final String ERROR_MESSAGE = "Invalid title format";

    private Pattern pattern;

    public TitleCheck(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public String check(JsonNode payload) {
        Matcher matcher = pattern.matcher(payload.get(Utils.PULL_REQUEST).get(Utils.TITLE).asText());
        if (!matcher.matches()) {
            return ERROR_MESSAGE;
        }

        return null;
    }
}
