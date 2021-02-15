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

import org.jboss.tyr.TestUtils;
import org.jboss.tyr.model.yaml.RegexDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

public class TitleCheckTest {

    private RegexDefinition titleRegexDefinition;
    private TitleCheck titleCheck;

    @BeforeEach
    public void before() {
        titleRegexDefinition = new RegexDefinition();
    }

    @Test
    public void testNullTitleParameter() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new TitleCheck(null));
    }

    @Test
    public void testNullCommitPatternParameter() {
        titleRegexDefinition.setPattern(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> new TitleCheck(titleRegexDefinition));
    }

    @Test
    public void testCheckSimpleRegexMatch() {
        titleRegexDefinition.setPattern(Pattern.compile("^Test.*PR$"));
        titleCheck = new TitleCheck(titleRegexDefinition);

        Assertions.assertNull(titleCheck.check(TestUtils.TEST_PAYLOAD), "Cannot match valid regex");
    }

    @Test
    public void testCheckSimpleRegexNonMatchReturnsExpectedMessage() {
        titleRegexDefinition.setPattern(Pattern.compile("can't.*match.*this"));
        titleRegexDefinition.setMessage("This is titleRegexDefinition message");
        titleCheck = new TitleCheck(titleRegexDefinition);

        Assertions.assertNotNull(titleCheck.check(TestUtils.TEST_PAYLOAD), "Matched invalid regex");
        Assertions.assertEquals(titleRegexDefinition.getMessage(), titleCheck.check(TestUtils.TEST_PAYLOAD),
            "Unexpected message returned");
    }

    @Test
    public void testCheckSimpleRegexNonMatchReturnsDefaultMessage() {
        titleRegexDefinition.setPattern(Pattern.compile("can't.*match.*this"));
        titleCheck = new TitleCheck(titleRegexDefinition);

        Assertions.assertEquals(TitleCheck.DEFAULT_MESSAGE, titleCheck.check(TestUtils.TEST_PAYLOAD),
            "Unexpected message returned");
    }
}
