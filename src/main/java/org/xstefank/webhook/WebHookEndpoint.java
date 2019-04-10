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
package org.xstefank.webhook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.net.URL;
import org.xstefank.api.GitHubAPI;
import org.xstefank.check.SkipCheck;
import org.xstefank.check.TemplateChecker;
import org.xstefank.whitelist.WhitelistProcessing;
import org.xstefank.verification.InvalidConfigurationException;
import org.xstefank.verification.VerificationHandler;
import org.xstefank.model.CommitStatus;
import org.xstefank.model.Utils;
import org.xstefank.model.yaml.FormatConfig;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;

import static org.xstefank.model.Utils.TEMPLATE_FORMAT_FILE;

@Path("/")
public class WebHookEndpoint {

    private static final FormatConfig config = readConfig();
    private static final WhitelistProcessing whitelistProcessing =
            WhitelistProcessing.IS_WHITELISTING_ENABLED ? new WhitelistProcessing(config) : null;

    private TemplateChecker templateChecker = new TemplateChecker(config);

    @POST
    @Path("/pull-request")
    @Consumes(MediaType.APPLICATION_JSON)
    public void processRequest(JsonNode payload) {
        if (whitelistProcessing != null) {
            processPRWithWhitelisting(payload);
        } else if (payload.has(Utils.PULL_REQUEST)) {
            processPullRequest(payload);
        }
    }

    private static FormatConfig readConfig() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        FormatConfig formatConfig;
        try {
            URL formatFileUrl = Utils.getFormatUrl();
            if (formatFileUrl != null)
                formatConfig = mapper.readValue(formatFileUrl.openStream(), FormatConfig.class);
            else {
                String configFileName = System.getProperty(TEMPLATE_FORMAT_FILE);
                if (configFileName == null) {
                    configFileName = Utils.getConfigDirectory() + "/format.yaml";
                }
                File configFile = new File(configFileName);
                formatConfig = mapper.readValue(configFile, FormatConfig.class);
            }
            VerificationHandler.verifyConfiguration(formatConfig);
            return formatConfig;
        } catch (IOException | InvalidConfigurationException e) {
            throw new IllegalArgumentException("Cannot load configuration file", e);
        }
    }

    private void processPullRequest(JsonNode prPayload) {
        if (!SkipCheck.shouldSkip(prPayload, config)) {
            String errorMessage = templateChecker.checkPR(prPayload);
            if (errorMessage != null) {
                GitHubAPI.updateCommitStatus(config.getRepository(),
                        prPayload.get(Utils.PULL_REQUEST).get(Utils.HEAD).get(Utils.SHA).asText(),
                        errorMessage.isEmpty() ? CommitStatus.SUCCESS : CommitStatus.ERROR,
                        config.getStatusUrl(),
                        errorMessage.isEmpty() ? "valid" : errorMessage, "PR format");
            }
        }
    }
    private void processPRWithWhitelisting(JsonNode payload) {
        if (payload.has(Utils.ISSUE)) {
            whitelistProcessing.processPRComment(payload);
        } else if (payload.has(Utils.PULL_REQUEST)) {
            processPullRequest(payload);
            if (!payload.get(Utils.ACTION).asText().matches("opened")) {
                return;
            }
            String username = payload.get(Utils.PULL_REQUEST).get(Utils.USER).get(Utils.LOGIN).asText();
            if (whitelistProcessing.isUserEligibleToRunCI(username)) {
                whitelistProcessing.triggerCI(payload.get(Utils.PULL_REQUEST));
            }
        }
    }
}
