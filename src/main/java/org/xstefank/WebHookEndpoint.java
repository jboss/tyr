package org.xstefank;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.egit.github.core.CommitStatus;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.event.PullRequestPayload;
import org.eclipse.egit.github.core.service.CommitService;
import org.jboss.logging.Logger;
import org.xstefank.check.TemplateChecker;
import org.xstefank.check.Violation;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Path("/")
public class WebHookEndpoint {

    private static final Logger log = Logger.getLogger(WebHookEndpoint.class);

    private static String oauthToken;

    @Context
    private UriInfo uriInfo;

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
    public void processPullRequest(JsonNode pullRequestPayload) throws IOException {
        log.info("pr received");
        JsonNode pullRequest = pullRequestPayload.get("pull_request");

        List<Violation> violations = TemplateChecker.check(pullRequest.get("body").asText());

        if (!violations.isEmpty()) {
            GitHubClient client = new GitHubClient();
            client.setOAuth2Token(oauthToken);

            CommitService commitService = new CommitService(client);

            JsonNode head = pullRequest.get("head");
            RepositoryId repo = RepositoryId.createFromId(head.get("repo").get("full_name").asText());
            String sha = head.get("sha").asText();

            CommitStatus commitStatus = new CommitStatus();
            commitStatus.setState(CommitStatus.STATE_PENDING);
            commitStatus.setCreatedAt(new Date());
            commitStatus.setDescription(violations.toString());
            commitStatus.setTargetUrl(uriInfo.getBaseUri().toString());

//            commitService.createStatus(repo, sha, commitStatus);
            log.info("repo - " + repo.generateId());
            log.info("sha - " + sha);

            log.info("status updated");
        }

    }

}
