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
package org.jboss.tyr.verification;

import org.jboss.tyr.model.Utils;
import org.jboss.tyr.model.yaml.FormatYaml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.jboss.tyr.TestUtils.FORMAT_CONFIG_CI;
import static org.jboss.tyr.TestUtils.YAML_DIR;
import static org.jboss.tyr.TestUtils.loadFormatFromYamlFile;

public class VerificationsTest {

    private static FormatYaml badFormatYaml;

    @BeforeAll
    public static void beforeClass() {
        badFormatYaml = loadFormatFromYamlFile(YAML_DIR + "/wrongTemplate.yaml");
    }

    @Test
    public void testReadValidFormatConfiguration() throws InvalidConfigurationException {
        VerificationHandler.verifyConfiguration(FORMAT_CONFIG_CI);
    }

    @Test
    public void testReadInvalidFormatConfiguration() {
        Assertions.assertThrows(InvalidConfigurationException.class, () -> VerificationHandler.verifyConfiguration(badFormatYaml));
    }

    @Test
    public void testNullParameterToVerify() {
        Assertions.assertThrows(NullPointerException.class, () -> VerificationHandler.verifyConfiguration(null));
    }
    @Test
    public void testRepositoryFormatVerificationRegex() {
        //Valid examples
        Assertions.assertTrue("a/repo".matches(Utils.REPOSITORY_FORMAT_VERIFICATION_REGEX));
        Assertions.assertTrue("a/.".matches(Utils.REPOSITORY_FORMAT_VERIFICATION_REGEX));
        Assertions.assertTrue("a/_".matches(Utils.REPOSITORY_FORMAT_VERIFICATION_REGEX));
        Assertions.assertTrue("a/-".matches(Utils.REPOSITORY_FORMAT_VERIFICATION_REGEX));
        Assertions.assertTrue("a-a123/.test.repo.".matches(Utils.REPOSITORY_FORMAT_VERIFICATION_REGEX));
        Assertions.assertTrue("a-A-a/_test_repo_".matches(Utils.REPOSITORY_FORMAT_VERIFICATION_REGEX));
        Assertions.assertTrue(("a".repeat(39)+"/-Test-repo-123-").matches(Utils.REPOSITORY_FORMAT_VERIFICATION_REGEX));
        Assertions.assertTrue(("a/" + "-".repeat(100)).matches(Utils.REPOSITORY_FORMAT_VERIFICATION_REGEX));

        //Invalid username
        Assertions.assertFalse("-a/test-repo".matches(Utils.REPOSITORY_FORMAT_VERIFICATION_REGEX));
        Assertions.assertFalse("/test-repo".matches(Utils.REPOSITORY_FORMAT_VERIFICATION_REGEX));
        Assertions.assertFalse("a-/test-repo".matches(Utils.REPOSITORY_FORMAT_VERIFICATION_REGEX));
        Assertions.assertFalse("a_a/test-repo".matches(Utils.REPOSITORY_FORMAT_VERIFICATION_REGEX));
        Assertions.assertFalse("a.a/test-repo".matches(Utils.REPOSITORY_FORMAT_VERIFICATION_REGEX));
        Assertions.assertFalse("a--a/test-repo".matches(Utils.REPOSITORY_FORMAT_VERIFICATION_REGEX));
        Assertions.assertFalse(("a".repeat(40) + "/test-repo").matches(Utils.REPOSITORY_FORMAT_VERIFICATION_REGEX));

        //Invalid repository names
        Assertions.assertFalse(("a/" + ".".repeat(101)).matches(Utils.REPOSITORY_FORMAT_VERIFICATION_REGEX));
        Assertions.assertFalse("a/".matches(Utils.REPOSITORY_FORMAT_VERIFICATION_REGEX));
    }
}
