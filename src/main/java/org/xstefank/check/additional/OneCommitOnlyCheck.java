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
package org.xstefank.check.additional;

import com.fasterxml.jackson.databind.JsonNode;
import org.xstefank.check.Check;
import org.xstefank.model.Utils;

public class OneCommitOnlyCheck implements Check {

    @Override
    public String check(JsonNode payload) {
        int numCommits = payload.get(Utils.PULL_REQUEST).get(Utils.COMMITS).asInt();

        if (numCommits > 1) {
            return "Please rebase the PR to only one commit";
        }

        return null;
    }
}
