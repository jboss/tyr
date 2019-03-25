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
package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import org.jboss.logging.Logger;
import org.xstefank.check.additional.AdditionalChecks;
import org.xstefank.model.yaml.FormatConfig;
import org.xstefank.model.yaml.Format;
import java.util.ArrayList;
import java.util.List;

public class TemplateChecker {

    public static final String TEMPLATE_FORMAT_FILE = "template.format.file";
    private static final Logger log = Logger.getLogger(TemplateChecker.class);

    private List<Check> checks;

    public TemplateChecker(FormatConfig config) {
        if (config == null || config.getFormat() == null) {
            throw new IllegalArgumentException("Input argument cannot be null!");
        }
        checks = registerChecks(config.getFormat());
    }

    public String checkPR(JsonNode payload) {
        log.info("checking PR");
        String description = "";

        if (checks.isEmpty()) {
            log.warn("No checks were requested in the configuration");
        }
        for (Check check : checks) {
            String message = check.check(payload);
            if (message != null) {
                description = message;
                break;
            }
        }

        return description;
    }

    private static List<Check> registerChecks(Format format) {
        List<Check> checks = new ArrayList<>();

        if (format.getTitle() != null) {
            checks.add(new TitleCheck(format.getTitle()));
        }

        if (format.getDescription() != null) {
            checks.add(new RequiredRowsCheck(format.getDescription().getRequiredRows()));
        }

        if (format.getCommit() != null) {
            checks.add(new LatestCommitCheck(format.getCommit()));
        }

        if (format.getAdditional() != null) {
            for (String additional : format.getAdditional()) {
                checks.add(AdditionalChecks.findCheck(additional));
            }
        }

        return checks;
    }
}
