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
package org.jboss.tyr.additional;

import io.quarkus.test.junit.QuarkusTest;
import org.jboss.tyr.InvalidPayloadException;
import org.jboss.tyr.TestUtils;
import org.jboss.tyr.additional.resource.DummyAdditionalCheck;
import org.jboss.tyr.additional.resource.DummyAdditionalCommand;
import org.jboss.tyr.check.TemplateChecker;
import org.jboss.tyr.whitelist.WhitelistProcessing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

/**
 * This test uses the generated JAR which can be found in the src/test/resource/generated/custom-resources.jar.
 * This JAR was generated using the following code:
 *
 * <code>
 * public static void generateAdditionalResources() {
 *     // create custom user jar
 *     JavaArchive customJar = ShrinkWrap.create(JavaArchive.class, "custom-resources.jar")
 *             .addClass(DummyAdditionalCheck.class)
 *             .addAsResource(new StringAsset("org.jboss.tyr.additional.resource.DummyAdditionalCheck"),
 *                     "META-INF/services/org.jboss.tyr.Check");
 *
 *     customJar.addClass(DummyAdditionalCommand.class)
 *             .addAsResource(new StringAsset("org.jboss.tyr.additional.resource.DummyAdditionalCommand"),
 *                     "META-INF/services/org.jboss.tyr.Command");
 *
 *     customJar.as(ZipExporter.class).exportTo(new File("target/custom-resources.jar"));
 * }
 * </code>
 */
@QuarkusTest
public class AdditionalResourcesTest {

    @Inject
    WhitelistProcessing whitelistProcessing;

    @Inject
    TemplateChecker templateChecker;

    @Test
    public void additionalChecksInvokedTest() throws InvalidPayloadException {
        DummyAdditionalCheck.clearCounter(); // incremented by different test checking the valid PR check invocation
        templateChecker.init(TestUtils.FORMAT_CONFIG);

        String result = templateChecker.checkPR(TestUtils.TEST_PAYLOAD);

        Assertions.assertEquals(DummyAdditionalCheck.getMessage(), result,
            "Additional check should have failed the validation");

        Assertions.assertEquals(1, DummyAdditionalCheck.getCounterValue(),
            "Additional check from custom jar should have been invoked");
    }

    @Test
    public void additionalCommandsInvokedTest() throws InvalidPayloadException {

        whitelistProcessing.init(TestUtils.FORMAT_CONFIG);
        whitelistProcessing.processPRComment(TestUtils.ISSUE_PAYLOAD);

        Assertions.assertTrue(DummyAdditionalCommand.isTriggered());
    }


    @Test
    @Disabled("Quarkus support for different config values")
    public void invalidPathAdditionalResourcesTest() throws InvalidPayloadException {
        templateChecker.init(TestUtils.FORMAT_CONFIG);
        whitelistProcessing.init(TestUtils.FORMAT_CONFIG);

        // should not fail, logs warning
        String result = templateChecker.checkPR(TestUtils.TEST_PAYLOAD);
        whitelistProcessing.processPRComment(TestUtils.ISSUE_PAYLOAD);

        Assertions.assertTrue(result.isEmpty());
        Assertions.assertFalse(DummyAdditionalCommand.isTriggered());
    }

    @Test
    @Disabled("Quarkus support for different config values")
    public void emptyAdditionalResourcesPropertyTest() throws InvalidPayloadException {
        templateChecker.init(TestUtils.FORMAT_CONFIG);
        whitelistProcessing.init(TestUtils.FORMAT_CONFIG);

        String result = templateChecker.checkPR(TestUtils.TEST_PAYLOAD);
        whitelistProcessing.processPRComment(TestUtils.ISSUE_PAYLOAD);

        Assertions.assertTrue(result.isEmpty());
        Assertions.assertFalse(DummyAdditionalCommand.isTriggered());
    }
}
