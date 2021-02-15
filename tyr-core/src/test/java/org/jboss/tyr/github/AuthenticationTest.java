package org.jboss.tyr.github;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URL;

//@RunWith(Arquillian.class)
//@RunAsClient
public class AuthenticationTest {

    private static Client restClient;
    private static final String RETURN_STATUS_UNAUTHORIZED_PATH = "/fakeGithub/ReturnStatusUnauthorized";
    private static final String RETURN_STATUS_INTERNAL_SERVER_ERROR_PATH = "/fakeGithub/ReturnStatusInternalServerError";

//    @Deployment(testable = false)
//    public static JavaArchive createDeployment() {
//        return ShrinkWrap
//                .create(JavaArchive.class, AuthenticationTest.class.getSimpleName() + ".jar")
//                .addClasses(FakeGitHub.class, JaxRsApplication.class);
//    }

//    @ArquillianResource
    private URL deploymentUrl;

    @BeforeAll
    public static void setUp() {
        restClient = ClientBuilder.newClient();
    }

//    @Test (expected=IllegalArgumentException.class)
    public void testAuthenticationStatusUnauthorized() {
        Assertions.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), getEndPointResponse(RETURN_STATUS_UNAUTHORIZED_PATH).getStatus());
        testAuthentication(RETURN_STATUS_UNAUTHORIZED_PATH);
    }

//    @Test (expected=IllegalArgumentException.class)
    public void testAuthenticationStatusInternalServerError() {
        Assertions.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), getEndPointResponse(RETURN_STATUS_INTERNAL_SERVER_ERROR_PATH).getStatus());
        testAuthentication(RETURN_STATUS_INTERNAL_SERVER_ERROR_PATH);
    }

    private Response getEndPointResponse(String endpointPath) {
        WebTarget target = restClient.target(UriBuilder.fromPath(deploymentUrl.toString()).path(endpointPath).build());

        return target.request()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "token ")
                .get();
    }

    private void testAuthentication(String endpointPath) {
//        GitHubService.getJSONReader(UriBuilder.fromPath(deploymentUrl.toString()).path(endpointPath).build());
    }

    @AfterAll
    public static void afterClass() {
        if (restClient != null) {
            restClient.close();
        }
    }
}
