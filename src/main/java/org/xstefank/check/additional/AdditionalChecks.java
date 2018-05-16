package org.xstefank.check.additional;

import org.xstefank.check.Check;

import java.util.HashMap;
import java.util.Map;

public class AdditionalChecks {

    private static Map<String, Check> additionalChecks = new HashMap<>();

    static {
        additionalChecks.put("TitleIssueLinkIncludedCheck", new TitleIssueLinkIncludedCheck());
        additionalChecks.put("OneCommitOnlyCheck", new OneCommitOnlyCheck());
    }

    public static Check findCheck(String name) {
        return additionalChecks.get(name);
    }

}
