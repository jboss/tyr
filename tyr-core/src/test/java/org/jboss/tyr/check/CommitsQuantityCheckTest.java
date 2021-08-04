/*
 * Copyright 2021 Red Hat, Inc, and individual contributors.
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
import org.jboss.tyr.InvalidPayloadException;
import org.jboss.tyr.TestUtils;
import org.jboss.tyr.model.yaml.CommitsQuantity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class CommitsQuantityCheckTest {

    @Inject
    CommitsQuantityCheck commitsQuantityCheck;

    private CommitsQuantity commitsQuantity;

    @BeforeEach
    public void before() {
        commitsQuantity = new CommitsQuantity();
    }

    @Test
    public void testNullCommitParameter() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> commitsQuantityCheck.initCheck(null));
    }

    @Test
    public void testNullCommitsQuantityParameter() {
        commitsQuantity.setQuantity(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> commitsQuantityCheck.initCheck(commitsQuantity));
    }

    @Test
    public void testNullCommitsMessageParameter() throws InvalidPayloadException {
        commitsQuantity.setMessage(null);
        commitsQuantity.setQuantity("5");
        commitsQuantityCheck.initCheck(commitsQuantity);

        Assertions.assertEquals(CommitsQuantityCheck.DEFAULT_MESSAGE, commitsQuantityCheck.check(TestUtils.TEST_PAYLOAD));
    }

    @Test
    public void testPayloadMatch() throws InvalidPayloadException {
        commitsQuantity.setQuantity("1");
        commitsQuantityCheck.initCheck(commitsQuantity);

        Assertions.assertNull(commitsQuantityCheck.check(TestUtils.TEST_PAYLOAD));
    }

    @Test
    public void testPayloadMismatch() throws InvalidPayloadException {
        commitsQuantity.setQuantity("2");
        commitsQuantity.setMessage("Test: This quantity of commits is not allowed!");
        commitsQuantityCheck.initCheck(commitsQuantity);

        Assertions.assertEquals("Test: This quantity of commits is not allowed!", commitsQuantityCheck.check(TestUtils.TEST_PAYLOAD));
    }

    @Test
    public void testRangePayloadMatch() throws InvalidPayloadException {
        commitsQuantity.setQuantity("1-2");
        commitsQuantityCheck.initCheck(commitsQuantity);

        Assertions.assertNull(commitsQuantityCheck.check(TestUtils.TEST_PAYLOAD));
    }

    @Test
    public void testRangePayloadMismatch() throws InvalidPayloadException {
        commitsQuantity.setQuantity("2-10");
        commitsQuantity.setMessage("Test: This quantity of commits is not allowed!");
        commitsQuantityCheck.initCheck(commitsQuantity);

        Assertions.assertEquals("Test: This quantity of commits is not allowed!", commitsQuantityCheck.check(TestUtils.TEST_PAYLOAD));
    }

    @Test
    public void testQuantityInvalidConfigNegativeRange() {
        commitsQuantity.setQuantity("20-10");

        Assertions.assertThrows(IllegalArgumentException.class, () -> commitsQuantityCheck.initCheck(commitsQuantity));
    }

    @Test
    public void testQuantityInvalidConfigNegativeNumber() {
        commitsQuantity.setQuantity("-999");

        Assertions.assertThrows(IllegalArgumentException.class, () -> commitsQuantityCheck.initCheck(commitsQuantity));
    }
}
