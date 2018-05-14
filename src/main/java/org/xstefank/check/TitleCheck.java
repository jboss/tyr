package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import org.jboss.logging.Logger;
import org.xstefank.model.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleCheck implements Check {

    private static final Logger log = Logger.getLogger(TitleCheck.class);
    private static final String ERROR_MESSAGE = "Invalid title format";

    private Pattern pattern;

    public TitleCheck(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public String check(JsonNode payload) {
        log.info("checking title");
        Matcher matcher = pattern.matcher(payload.get(Utils.PULL_REQUEST).get(Utils.TITLE).asText());
        if (!matcher.matches()) {
            return ERROR_MESSAGE;
        }

        return null;
    }
}
