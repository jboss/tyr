package org.xstefank.check;

import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateChecker {

    private static final Logger log = Logger.getLogger(TemplateChecker.class);

    private static Pattern pattern = Pattern.compile("Upstream Issue: (https://issues.jboss.org/browse/WFLY-\\d+|Upstream not required)\n" +
            "Upstream PR: #\\d+\n" +
            "Issue: https://issues.jboss.org/browse/JBEAP-\\d+");

    public static List<Violation> check(String s) {
        log.info("going to check s " + s);
        Matcher matcher = pattern.matcher(s);
        if (!matcher.matches()) {
            log.info("not matched");
        }

        return new ArrayList<>();
    }

}
