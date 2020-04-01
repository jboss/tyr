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
package org.jboss.tyr.check;

import io.quarkus.test.junit.QuarkusTest;
import org.jboss.tyr.model.yaml.FormatYaml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class TemplateCheckerTest {

    @Inject
    TemplateChecker templateChecker;

    @Test
    public void testNullConfigParameter() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> templateChecker.init(null));
    }

    @Test
    public void testNullFormatParameter() {
        FormatYaml testConfig = new FormatYaml();
        testConfig.setFormat(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> templateChecker.init(testConfig));
    }
}
