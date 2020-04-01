package org.jboss.tyr.mock;

import io.quarkus.test.Mock;
import org.jboss.tyr.InvalidPayloadException;
import org.jboss.tyr.TestUtils;
import org.jboss.tyr.github.GitHubService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.json.JsonArray;
import javax.json.JsonObject;

@Mock
@Named("mock")
@ApplicationScoped
public class GitHubServiceMock extends GitHubService {

    @Override
    public JsonArray getCommitsJSON(JsonObject prPayload) throws InvalidPayloadException {
        return TestUtils.TEST_COMMITS_PAYLOAD;
    }

    @Override
    public JsonObject getPullRequestJSON(JsonObject issuePayload) throws InvalidPayloadException {
        return TestUtils.TEST_PAYLOAD;
    }
}
