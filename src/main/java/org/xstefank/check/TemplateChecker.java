package org.xstefank.check;

import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateChecker {

    private static final Logger log = Logger.getLogger(TemplateChecker.class);

    public static List<Violation> check(String s) {
        log.info("going to check: " + s);
        List<Violation> violations = new ArrayList<>();
        
        if (!s.contains("WFLY")) {
            log.info("not matched");
            violations.add(new Violation("not matched", "description invalid"));
        }

        return violations;
    }

}
