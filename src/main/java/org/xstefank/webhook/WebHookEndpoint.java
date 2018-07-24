package org.xstefank.webhook;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;
import org.xstefank.check.TemplateChecker;

import com.fasterxml.jackson.databind.JsonNode;

@Path("/")
public class WebHookEndpoint {

    private static final Logger log = Logger.getLogger(WebHookEndpoint.class);

    private TemplateChecker templateChecker = new TemplateChecker();

    @POST
    @Path("/pull-request")
    @Consumes(MediaType.APPLICATION_JSON)
    public void processPullRequest(JsonNode pullRequestPayload) {
        templateChecker.checkPR(pullRequestPayload);
    }
}
