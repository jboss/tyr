package org.xstefank.ci;

import java.util.HashMap;
import java.util.Map;

public class CILoader {

    private static Map<String, ContinuousIntegration> CIs = new HashMap<>();

    static {
        addCI(TeamCityCI.NAME, new TeamCityCI());
    }

    public static ContinuousIntegration getCI(String key) {
        return CIs.get(key);
    }

    static void addCI(String key, ContinuousIntegration continuousIntegration) {
        CIs.put(key, continuousIntegration);
    }
}
