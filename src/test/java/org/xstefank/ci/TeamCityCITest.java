package org.xstefank.ci;

import org.junit.Test;

public class TeamCityCITest {

    @Test(expected = IllegalArgumentException.class)
    public void testTCInitWithoutPropertiesSet() {
        new TeamCityCI().init();
    }
}