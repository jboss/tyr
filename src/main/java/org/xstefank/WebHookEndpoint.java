package org.xstefank;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.jboss.logging.Logger;
import org.xstefank.check.TemplateChecker;
import org.xstefank.check.Violation;
import org.xstefank.model.StatusPayload;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;

@Path("/")
public class WebHookEndpoint {

    private static final String GITHUB_BASE = "https://api.github.com";
    private static final String CONFIG_FILE = "config.properties";
    private static final String TOKEN_PROPERTY = "github.oauth.token";
    private static final String TOKEN_ENV = "GITHUB_OAUTH_TOKEN";
    private static final String JBOSS_CONFIG_DIR = "jboss.server.config.dir";

    private static final Logger log = Logger.getLogger(WebHookEndpoint.class);

    private static String oauthToken;

    @Context
    private UriInfo uriInfo;

    static {
        oauthToken = readToken();
    }

    @POST
    @Path("/pull-request")
    @Consumes(MediaType.APPLICATION_JSON)
    public void processPullRequest(JsonNode pullRequestPayload) throws IOException, URISyntaxException {
        log.info("pr received");
        JsonNode pullRequest = pullRequestPayload.get("pull_request");

        List<Violation> violations = TemplateChecker.check(pullRequest.get("body").asText());

        if (!violations.isEmpty()) {
            log.info("invalid desc");
            GitHubClient client = new GitHubClient();
            client.setOAuth2Token(oauthToken);

            JsonNode head = pullRequest.get("head");
            RepositoryId repo = RepositoryId.createFromId(head.get("repo").get("full_name").asText());
            String sha = head.get("sha").asText();

            Client resteasyClient = ClientBuilder.newClient();
            URI statusUri = UriBuilder
                    .fromUri(GITHUB_BASE)
                    .path("/repos")
                    .path("/" + repo.generateId())
                    .path("/statuses")
                    .path("/" + sha)
                    .build();

            log.info("token - " + oauthToken);

            WebTarget target = resteasyClient.target(statusUri);

            Entity<StatusPayload> json = Entity.json(new StatusPayload("error",
                    uriInfo.getBaseUri().toString(), violations.toString(), "jboss-set"));

            Response response = target.request()
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .header("Authorization", "token " + oauthToken)
                    .post(json);


            log.info("status updated " + response.getStatus() + " | " + response.getEntity() + " | " + response.toString());
        } else {
            log.info("valid desc");
            GitHubClient client = new GitHubClient();
            client.setOAuth2Token(oauthToken);

            JsonNode head = pullRequest.get("head");
            RepositoryId repo = RepositoryId.createFromId(head.get("repo").get("full_name").asText());
            String sha = head.get("sha").asText();

            Client resteasyClient = ClientBuilder.newClient();
            URI statusUri = UriBuilder
                    .fromUri(GITHUB_BASE)
                    .path("/repos")
                    .path("/" + repo.generateId())
                    .path("/statuses")
                    .path("/" + sha)
                    .build();

            WebTarget target = resteasyClient.target(statusUri);

            Entity<StatusPayload> json = Entity.json(new StatusPayload("success",
                    uriInfo.getBaseUri().toString(), "", "jboss-set"));

            Response response = target.request()
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .header("Authorization", "token " + oauthToken)
                    .post(json);


            log.info("status updated " + response.getStatus() + response.getEntity() + response.toString());

        }

    }

    private static String readToken() {
        String token;
        token = readTokenFromProperties("src/main/resources", CONFIG_FILE);
        token = token == null ? readTokenFromProperties(System.getProperty(JBOSS_CONFIG_DIR), CONFIG_FILE) : token;
        return token == null ? System.getenv(TOKEN_ENV) : token;
    }

    private static String readTokenFromProperties(String dirName, String fileName) {
        InputStream is = null;
        File dir = new File(dirName);
        File fileProp = new File(dir, fileName);

        try {
            is = new FileInputStream(fileProp);
            Properties properties = new Properties();
            properties.load(is);

            if (properties.getProperty(TOKEN_PROPERTY) != null) {
                return properties.getProperty(TOKEN_PROPERTY);
            }
        } catch (Exception e) {
            // intentionally ignored
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                // intentionally ignored
            }
        }

        return null;
    }

}
