package org.xstefank;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.jboss.logging.Logger;
import org.xstefank.check.TemplateChecker;
import org.xstefank.check.Violation;
import org.xstefank.model.CommitStatus;
import org.xstefank.model.StatusPayload;
import org.xstefank.model.Utils;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static org.xstefank.model.Utils.readTokenFromProperties;

@Path("/")
public class WebHookEndpoint {

    private static final Logger log = Logger.getLogger(WebHookEndpoint.class);

    private static String oauthToken = readToken();

    @Context
    private UriInfo uriInfo;

    @POST
    @Path("/pull-request")
    @Consumes(MediaType.APPLICATION_JSON)
    public void processPullRequest(JsonNode pullRequestPayload) {
        JsonNode pullRequest = pullRequestPayload.get(Utils.PULL_REQUEST);

        List<Violation> violations = TemplateChecker.check(pullRequest.get(Utils.BODY).asText());

        if (!violations.isEmpty()) {
            processResponse(pullRequest, CommitStatus.ERROR, violations);
        } else {
            processResponse(pullRequest, CommitStatus.SUCCESS, null);
        }

    }

    private void processResponse(JsonNode pullRequest, CommitStatus status, List<Violation> violations) {
        JsonNode head = pullRequest.get(Utils.HEAD);
        String repoitory = head.get(Utils.REPO).get(Utils.FULL_NAME).asText();
        String sha = head.get(Utils.SHA).asText();

        Client resteasyClient = ClientBuilder.newClient();
        URI statusUri = UriBuilder
                .fromUri(Utils.GITHUB_BASE)
                .path("/repos")
                .path("/" + repoitory)
                .path("/statuses")
                .path("/" + sha)
                .build();

        WebTarget target = resteasyClient.target(statusUri);

        String description = violations != null ? violations.toString() : "Valid description";
        Entity<StatusPayload> json = Entity.json(new StatusPayload(status.toString(),
                uriInfo.getBaseUri().toString(), description, "Template check"));

        Response response = target.request()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "token " + oauthToken)
                .post(json);

        log.info("Status update: " + response.getStatus());
        response.close();
    }

    private static String readToken() {
        String token;
        token = readTokenFromProperties("src/main/resources", Utils.CONFIG_FILE);
        token = token == null ? readTokenFromProperties(System.getProperty(Utils.JBOSS_CONFIG_DIR), Utils.CONFIG_FILE) : token;
        return token == null ? System.getenv(Utils.TOKEN_ENV) : token;
    }


}
