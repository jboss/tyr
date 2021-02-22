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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.jboss.logging.Logger;
import org.jboss.tyr.Check;
import org.jboss.tyr.InvalidPayloadException;
import org.jboss.tyr.github.GitHubService;
import org.jboss.tyr.model.AdditionalResourcesLoader;
import org.jboss.tyr.model.CommitStatus;
import org.jboss.tyr.model.TyrConfiguration;
import org.jboss.tyr.model.Utils;
import org.jboss.tyr.model.yaml.Format;
import org.jboss.tyr.model.yaml.FormatYaml;
import org.jboss.tyr.verification.InvalidConfigurationException;
import org.jboss.tyr.verification.VerificationHandler;
import org.jboss.tyr.whitelist.WhitelistProcessing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import javax.json.JsonObject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TemplateChecker {

    private static final Logger log = Logger.getLogger(TemplateChecker.class);

    @Inject
    TyrConfiguration configuration;

    @Inject
    WhitelistProcessing whitelistProcessing;

    @Inject
    GitHubService gitHubService;

    @Inject
    @New
    CommitMessagesCheck commitMessagesCheck;

    @Inject
    AdditionalResourcesLoader additionalResourcesLoader;

    @Inject
    SkipCheck skipCheck;

    private FormatYaml format;
    private List<Check> checks;

    @PostConstruct
    public void init() {
        format = readConfig();
        if (format == null || format.getFormat() == null) {
            throw new IllegalArgumentException("Input format yaml cannot be null");
        }

        whitelistProcessing.init(format);
        checks = registerChecks(format.getFormat());
    }

    public String processPullRequest(JsonObject payload) throws InvalidPayloadException {
        if (configuration.whitelistEnabled()) {
            return processPRWithWhitelisting(payload);
        } else if (payload.getJsonObject(Utils.PULL_REQUEST) != null) {
            return processPR(payload);
        }

        return "Received an invalid Pull Request JSON";
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

    private FormatYaml readConfig() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        FormatYaml formatYaml;
        try {
            if (configuration.formatFileUrl().isPresent())
                formatYaml = mapper.readValue(new URL(configuration.formatFileUrl().get()).openStream(), FormatYaml.class);
            else {
                File configFile = new File(configuration.configFileName().orElse(Utils.getConfigDirectory() + "/format.yaml"));
                formatYaml = mapper.readValue(configFile, FormatYaml.class);
            }
            VerificationHandler.verifyConfiguration(formatYaml);
            return formatYaml;
        } catch (IOException | InvalidConfigurationException e) {
            throw new IllegalArgumentException("Cannot load configuration file", e);
        }
    }

    private String processPR(JsonObject prPayload) throws InvalidPayloadException {
        if (!skipCheck.shouldSkip(prPayload, format)) {
            String errorMessage = checkPR(prPayload);
            if (errorMessage != null) {
                gitHubService.updateCommitStatus(format.getRepository(),
                    prPayload.getJsonObject(Utils.PULL_REQUEST).getJsonObject(Utils.HEAD).getString(Utils.SHA),
                    errorMessage.isEmpty() ? CommitStatus.SUCCESS : CommitStatus.ERROR,
                    format.getStatusUrl(),
                    errorMessage.isEmpty() ? "valid" : errorMessage, "PR format");
            }

            return errorMessage;
        }

        return "";
    }

    private String processPRWithWhitelisting(JsonObject payload) throws InvalidPayloadException {
        if (payload.getJsonObject(Utils.ISSUE) != null) {
            whitelistProcessing.processPRComment(payload);
            return "";
        } else if (payload.getJsonObject(Utils.PULL_REQUEST) != null) {
            String errorMessage = processPullRequest(payload);
            if (payload.getString(Utils.ACTION).matches("opened")) {
                String username = payload.getJsonObject(Utils.PULL_REQUEST)
                    .getJsonObject(Utils.USER)
                    .getString(Utils.LOGIN);
                if (whitelistProcessing.isUserEligibleToRunCI(username)) {
                    whitelistProcessing.triggerCI(payload.getJsonObject(Utils.PULL_REQUEST));
                }
            }

            return errorMessage;
        }

        return "Received an invalid JSON not representing a valid Pull Request or Issue format";
    }
}
