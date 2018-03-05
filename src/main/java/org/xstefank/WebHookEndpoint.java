package org.xstefank;

import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.event.PullRequestPayload;
import org.jboss.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.Properties;

@Path("/")
public class WebHookEndpoint {

    private static final Logger log = Logger.getLogger(WebHookEndpoint.class);

    private static String oauthToken;

    static {
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("config.properties");

        Properties config = new Properties();
        try {
            config.load(is);
        } catch (Exception e) {
            log.info("properties cannot be loaded");
        }

        oauthToken = config.getProperty("github.oauth.token");
        if (oauthToken == null) {
            oauthToken = System.getProperty("GITHUB_OAUTH_TOKEN");
        }
    }

    @POST
    @Path("/pull-request")
    @Consumes(MediaType.APPLICATION_JSON)
    public void processPullRequest(Object pullRequest) {
        log.info(pullRequest);
    }

}
