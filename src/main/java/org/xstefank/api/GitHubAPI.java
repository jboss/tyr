package org.xstefank.api;

import com.fasterxml.jackson.databind.JsonNode;

import org.jboss.logging.Logger;
import org.xstefank.model.CommitStatus;
import org.xstefank.model.StatusPayload;
import org.xstefank.model.Utils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static org.xstefank.model.Utils.readTokenFromProperties;

public class GitHubAPI {

    private static String oauthToken = readToken();
    private static final Logger log = Logger.getLogger(GitHubAPI.class);

    public static void updatePRStatus(String repository, String sha, CommitStatus status,
                                      String targetUrl, String description, String context) {

        Client resteasyClient = ClientBuilder.newClient();
        URI statusUri = UriBuilder
                .fromUri(Utils.GITHUB_BASE)
                .path("/repos")
                .path("/" + repository)
                .path("/statuses")
                .path("/" + sha)
                .build();

        WebTarget target = resteasyClient.target(statusUri);

        Entity<StatusPayload> json = Entity.json(new StatusPayload(status.toString(),
                targetUrl, description, context));

        Response response = target.request()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "token " + oauthToken)
                .post(json);

        log.info("Status update: " + response.getStatus());
        response.close();
    }

    public static JsonNode getJsonWithCommits(JsonNode payload) {
        Client restClient = ClientBuilder.newClient();
        WebTarget target = restClient.target(getUriFromPayload(payload));

        Response response = target.request()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "token " + oauthToken)
                .get();

        JsonNode newJson = response.readEntity(JsonNode.class);
        response.close();

        return newJson;
    }

    private static URI getUriFromPayload(JsonNode payload) {
        String url = payload.get(Utils.PULL_REQUEST).get(Utils.URL).asText();
        URI commitsUri = UriBuilder.fromPath(url)
                .path(Utils.COMMITS)
                .build();
        return commitsUri;
    }

    private static String readToken() {
        String token = readTokenFromProperties(System.getProperty(Utils.JBOSS_CONFIG_DIR), Utils.CONFIG_FILE);
        return token == null || token.isEmpty() ? System.getenv(Utils.TOKEN_ENV) : token;
    }
}
