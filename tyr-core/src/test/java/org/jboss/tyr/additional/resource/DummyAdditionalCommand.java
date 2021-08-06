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
package org.jboss.tyr.additional.resource;

import org.jboss.tyr.Command;
import org.jboss.tyr.CIOperations;

import javax.json.JsonObject;

public class DummyAdditionalCommand implements Command {

    private static boolean triggered = false;

    @Override
    public void process(JsonObject jsonObject, CIOperations operations) {
        triggered = true;
    }

    @Override
    public String getRegex() {
        return "dummy-command";
    }

    public static boolean isTriggered() {
        return triggered;
    }
}
