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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.regex.Pattern;

import static java.util.Collections.singletonList;

public class RequiredRowsCheckTest {

    private static RegexDefinition row;
    private RequiredRowsCheck requiredRowsCheck;

    @BeforeAll
    public static void beforeClass() {
        row = new RegexDefinition();
        row.setPattern(Pattern.compile("^Test.*description$"));
        row.setMessage("Does not match");
    }

    @BeforeEach
    public void before() {
        requiredRowsCheck = new RequiredRowsCheck(singletonList(row));
    }

    @Test
    public void checkSimpleRegexMatch() {
        Assertions.assertNull(requiredRowsCheck.check(TestUtils.TEST_PAYLOAD), "Cannot match valid description");
    }

    @Test
    public void checkSimpleRegexNonMatch() {
        row.setPattern(Pattern.compile("can't.*match.*this"));
        Assertions.assertNotNull(requiredRowsCheck.check(TestUtils.TEST_PAYLOAD), "Matched invalid description");
    }

    @Test
    public void testIfNullParameterThrowsException() {
        RequiredRowsCheck requiredRowsCheck = new RequiredRowsCheck(null);
        Assertions.assertThrows(NullPointerException.class, () -> requiredRowsCheck.check(TestUtils.TEST_PAYLOAD));
    }

    @Test
    public void testIfEmptyPayloadThrowsException() {
        Assertions.assertThrows(NullPointerException.class, () -> requiredRowsCheck.check(TestUtils.EMPTY_PAYLOAD));
    }

    @Test
    public void testMultipleFailedLines() {
        RegexDefinition secondRow = new RegexDefinition();
        String expectedErrorMessage = "Second error message";

        secondRow.setPattern(Pattern.compile("(?!.*)"));
        secondRow.setMessage(expectedErrorMessage);

        requiredRowsCheck = new RequiredRowsCheck(Arrays.asList(row, secondRow));
        String errorMessage = requiredRowsCheck.check(TestUtils.TEST_PAYLOAD);

        Assertions.assertNotNull(errorMessage);
        Assertions.assertEquals(expectedErrorMessage, errorMessage);
    }
}
