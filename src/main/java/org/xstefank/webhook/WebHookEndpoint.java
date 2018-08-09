package org.xstefank.webhook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.jboss.logging.Logger;
import org.xstefank.check.SkipChecker;
import org.xstefank.check.TemplateChecker;
import org.xstefank.model.Utils;
import org.xstefank.model.yaml.FormatConfig;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import static org.xstefank.check.TemplateChecker.TEMPLATE_FORMAT_FILE;

@Path("/")
public class WebHookEndpoint {

    private static final Logger log = Logger.getLogger(WebHookEndpoint.class);
    private FormatConfig config = readConfig();
    private TemplateChecker templateChecker = new TemplateChecker(config);

    @POST
    @Path("/pull-request")
    @Consumes(MediaType.APPLICATION_JSON)
    public void processPullRequest(JsonNode pullRequestPayload) {
        if (!SkipChecker.shouldSkip(pullRequestPayload, config)) {
            templateChecker.checkPR(pullRequestPayload);
        }
    }

    private static FormatConfig readConfig() {
        String configFileName = System.getProperty(TEMPLATE_FORMAT_FILE);
        if (configFileName == null) {
            configFileName = System.getProperty(Utils.JBOSS_CONFIG_DIR) + "/format.yaml";
        }
        log.info(configFileName);
        File configFile = new File(configFileName);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            return mapper.readValue(configFile, FormatConfig.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot load configuration file", e);
        }
    }
}