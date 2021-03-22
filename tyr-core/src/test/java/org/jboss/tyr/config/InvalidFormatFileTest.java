/*
 * Copyright 2019-2021 Red Hat, Inc, and individual contributors.
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
package org.jboss.tyr.config;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import org.jboss.tyr.TestUtils;
import org.jboss.tyr.check.TemplateChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Map;

@QuarkusTest
@TestProfile(InvalidFormatFileTest.Profile.class)
class InvalidFormatFileTest {
    @Inject
    TemplateChecker templateChecker;

    public static class Profile implements QuarkusTestProfile {
        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of("tyr.template.format.file", "src/test/resources/yaml/wrongTemplateInvalidFormat.yaml");
        }
    }

    @Test
    public void testInvalidFormatYaml() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> templateChecker.checkPR(TestUtils.TEST_PAYLOAD),
                "Expected IllegalArgumentException to be thrown when file contains invalid format");
    }
}
