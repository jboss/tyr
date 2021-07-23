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

import org.jboss.tyr.Check;
import org.jboss.tyr.InvalidPayloadException;
import org.jboss.tyr.github.GitHubService;
import org.jboss.tyr.model.Utils;
import org.jboss.tyr.model.yaml.LabelDetails;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

@ApplicationScoped
public class AddTargetBranchLabelCheck implements Check {

    static final String DEFAULT_DESCRIPTION = "No description";
    static final String DEFAULT_WHITE_COLOR = "ffffff";

    @Inject
    GitHubService gitHubService;

    private String description = DEFAULT_DESCRIPTION;

    private String color = DEFAULT_WHITE_COLOR;

    public void initCheck(LabelDetails labelDetails) {
        if (labelDetails.getColor() != null && !labelDetails.getColor().toLowerCase().matches(Utils.COLOR_VERIFICATION)) {
            throw new IllegalArgumentException(
                    "Wrong color format, has to be lower character hexadecimal color code, without the leading #.");
        }
        if (labelDetails.getDescription() != null) {
            this.description = labelDetails.getDescription();
        }
        if (labelDetails.getColor() != null) {
            this.color = labelDetails.getColor().toLowerCase();
        }
    }

    @Override
    public String check(JsonObject payload) throws InvalidPayloadException {
        String repository = payload.getJsonObject(Utils.REPOSITORY).getString(Utils.FULL_NAME);
        int pullRequestNumber = payload.getInt(Utils.NUMBER);
        String targetBranch = payload.getJsonObject(Utils.PULL_REQUEST).getJsonObject(Utils.BASE).getString(Utils.REF);

        JsonArray labels = payload.getJsonObject(Utils.PULL_REQUEST).getJsonArray(Utils.LABELS);
        if (labels != null) {
            for (JsonValue label : labels) {
                if (label.asJsonObject().getString(Utils.NAME).equals(targetBranch)) {
                    return null;
                }
            }
        }

        gitHubService.addLabelToPullRequest(repository, pullRequestNumber, targetBranch, description, color);
        return null;
    }
}
