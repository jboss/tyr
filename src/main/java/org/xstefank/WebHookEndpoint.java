package org.xstefank;

import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.jboss.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

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

    @GET
    @Path("/get")
    public String testGet() throws IOException {

        GitHubClient client = new GitHubClient();
        client.setOAuth2Token(oauthToken);

        PullRequestService service = new PullRequestService(client);
        RepositoryId repo = new RepositoryId("xstefank", "test-repo");
        List<PullRequest> open = service.getPullRequests(repo, "open");

        return open.stream().map(pr -> pr.getTitle()).collect(Collectors.toList()).toString();
    }

}
