/*
 * Copyright 2019 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.tyr.ci;

import org.jboss.tyr.InvalidPayloadException;
import org.jboss.tyr.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TeamCityCITest {

    @Test
    public void testTCInitWithoutPropertiesSet() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new TeamCityCI().init());
    }

    @Test
    public void testTriggerBuildNullParameters() {
        Assertions.assertThrows(InvalidPayloadException.class, () -> new TeamCityCI().triggerBuild(null));
    }

    @Test
    public void testTriggerFailedBuildNullParameters() {
        Assertions.assertThrows(InvalidPayloadException.class, () -> new TeamCityCI().triggerFailedBuild(null));
    }

    @Test
    public void testTriggerBuildPrPayloadMissingPullRequestParameter() {
        Assertions.assertThrows(InvalidPayloadException.class, () -> new TeamCityCI().triggerBuild(TestUtils.EMPTY_PAYLOAD));
    }

    @Test
    public void testTriggerFailedBuildPrPayloadMissingPullRequestParameter() {
        Assertions.assertThrows(InvalidPayloadException.class, () -> new TeamCityCI().triggerFailedBuild(TestUtils.EMPTY_PAYLOAD));
    }

    @Test
    public void testTriggerBuildConfigFileMissing() {
        Assertions.assertThrows(InvalidPayloadException.class, () -> new TeamCityCI().triggerFailedBuild(TestUtils.PULL_REQUEST_PAYLOAD));
    }

    @Test
    public void testTriggerFailedBuildConfigFileMissing() {
        Assertions.assertThrows(InvalidPayloadException.class, () -> new TeamCityCI().triggerFailedBuild(TestUtils.PULL_REQUEST_PAYLOAD));
    }
}
