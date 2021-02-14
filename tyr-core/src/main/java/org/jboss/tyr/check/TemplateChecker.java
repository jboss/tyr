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
package org.jboss.tyr.check;

import org.jboss.logging.Logger;
import org.jboss.tyr.Check;
import org.jboss.tyr.InvalidPayloadException;
import org.jboss.tyr.model.AdditionalResourcesLoader;
import org.jboss.tyr.model.Utils;
import org.jboss.tyr.model.yaml.Format;
import org.jboss.tyr.model.yaml.FormatYaml;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TemplateChecker {

    private static final Logger log = Logger.getLogger(TemplateChecker.class);

    @Inject
    @New
    CommitMessagesCheck commitMessagesCheck;

    @Inject
    AdditionalResourcesLoader additionalResourcesLoader;

    private List<Check> checks;

    public void init(FormatYaml config) {
        if (config == null || config.getFormat() == null) {
            throw new IllegalArgumentException("Input argument cannot be null");
        }
        checks = registerChecks(config.getFormat());
    }

    /**
     * Verifies the pull request payload against a set of defined checks
     *
     * @param payload the PR payload JSON received from GitHub
     * @return error message or empty string if there is no failure found
     */
    public String checkPR(JsonObject payload) throws InvalidPayloadException {
        log.debug("checking PR" + Utils.LINE_SEPARATOR + payload);
        String errorMessage = "";

        if (checks.isEmpty()) {
            log.warn("No checks were requested in the configuration");
            return "";
        }

        for (Check check : checks) {
            String message = check.check(payload);
            if (message != null) {
                errorMessage = message;
                break;
            }
        }

        return errorMessage;
    }

    private List<Check> registerChecks(Format format) {
        List<Check> checks = new ArrayList<>();

        if (format.getTitle() != null) {
            checks.add(new TitleCheck(format.getTitle()));
        }

        if (format.getDescription() != null) {
            checks.add(new DescriptionCheck(format.getDescription().getRequiredRows(),
                    format.getDescription().getOptionalRows()));
        }

        if (format.getCommit() != null) {
            commitMessagesCheck.setRegex(format.getCommit());
            checks.add(commitMessagesCheck);
        }

        checks.addAll(additionalResourcesLoader.getAdditionalChecks());

        return checks;
    }
}
