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

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.tyr.TestUtils;
import org.jboss.tyr.additional.resource.DummyAdditionalCheck;
import org.jboss.tyr.additional.resource.DummyAdditionalCommand;
import org.jboss.tyr.check.TemplateChecker;
import org.jboss.tyr.model.Utils;
import org.jboss.tyr.whitelist.WhitelistProcessing;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import static org.jboss.tyr.model.Utils.ADDITIONAL_RESOURCES_PROPERTY;

public class AdditionalResourcesTest {

    @BeforeClass
    public static void beforeClass() {
        // create custom user jar
        JavaArchive customJar = ShrinkWrap.create(JavaArchive.class, "custom-resources.jar")
                .addClass(DummyAdditionalCheck.class)
                .addAsResource(new StringAsset("org.jboss.tyr.additional.resource.DummyAdditionalCheck"),
                        "META-INF/services/org.jboss.tyr.Check");

        customJar.addClass(DummyAdditionalCommand.class)
                .addAsResource(new StringAsset("org.jboss.tyr.additional.resource.DummyAdditionalCommand"),
                        "META-INF/services/org.jboss.tyr.Command");

        customJar.as(ZipExporter.class).exportTo(new File("target/custom-resources.jar"));
        System.setProperty(Utils.TYR_CONFIG_DIR, TestUtils.TARGET_DIR);
    }

    @Test
    public void additionalChecksInvokedTest() {
        System.setProperty(ADDITIONAL_RESOURCES_PROPERTY, "target/custom-resources.jar");
        TemplateChecker templateChecker = new TemplateChecker(TestUtils.FORMAT_CONFIG);

        String result = templateChecker.checkPR(TestUtils.TEST_PAYLOAD);

        Assert.assertEquals("Additional check should have failed the validation",
                DummyAdditionalCheck.getMessage(), result);

        Assert.assertEquals("Additional check from custom jar should have been invoked",
                1, DummyAdditionalCheck.getCounterValue());
    }

    @Test
    public void additionalCommandsInvokedTest() {
        System.setProperty(ADDITIONAL_RESOURCES_PROPERTY, "target/custom-resources.jar");

        WhitelistProcessing whitelistProcessing = new WhitelistProcessing(TestUtils.FORMAT_CONFIG);
        whitelistProcessing.processPRComment(TestUtils.ISSUE_PAYLOAD);

        Assert.assertTrue(DummyAdditionalCommand.isTriggered());
    }


    @Test
    public void invalidPathAdditionalResourcesTest() {
        System.setProperty(ADDITIONAL_RESOURCES_PROPERTY, "target/invalid-path.jar");
        TemplateChecker templateChecker = new TemplateChecker(TestUtils.FORMAT_CONFIG);
        WhitelistProcessing whitelistProcessing = new WhitelistProcessing(TestUtils.FORMAT_CONFIG);

        // should not fail, logs warning
        String result = templateChecker.checkPR(TestUtils.TEST_PAYLOAD);
        whitelistProcessing.processPRComment(TestUtils.ISSUE_PAYLOAD);

        Assert.assertTrue(result.isEmpty());
        Assert.assertFalse(DummyAdditionalCommand.isTriggered());
    }

    @Test
    public void emptyAdditionalResourcesPropertyTest() {
        System.clearProperty(ADDITIONAL_RESOURCES_PROPERTY);
        TemplateChecker templateChecker = new TemplateChecker(TestUtils.FORMAT_CONFIG);
        WhitelistProcessing whitelistProcessing = new WhitelistProcessing(TestUtils.FORMAT_CONFIG);

        String result = templateChecker.checkPR(TestUtils.TEST_PAYLOAD);
        whitelistProcessing.processPRComment(TestUtils.ISSUE_PAYLOAD);

        Assert.assertTrue(result.isEmpty());
        Assert.assertFalse(DummyAdditionalCommand.isTriggered());
    }

    @After
    public void after() {
        System.clearProperty(ADDITIONAL_RESOURCES_PROPERTY);
    }

    @AfterClass
    public static void afterClass() {
        TestUtils.deleteFileIfExists(new File("target/custom-resources.jar"));
        System.clearProperty(Utils.TYR_CONFIG_DIR);
    }
}
