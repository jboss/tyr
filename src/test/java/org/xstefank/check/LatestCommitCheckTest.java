package org.xstefank.check;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xstefank.TestUtils;
import org.xstefank.api.GitHubAPI;
import org.xstefank.model.yaml.RegexDefinition;
import java.util.regex.Pattern;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GitHubAPI.class)
public class LatestCommitCheckTest {

    private RegexDefinition commitRegexDefinition;
    private LatestCommitCheck latestCommitCheck;

    @Before
    public void setUp() {
        commitRegexDefinition = new RegexDefinition();
        PowerMockito.suppress(method(GitHubAPI.class, TestUtils.READ_TOKEN));
        PowerMockito.stub(method(GitHubAPI.class, TestUtils.GET_JSON_WITH_COMMITS, JsonNode.class)).toReturn(TestUtils.TEST_COMMITS_PAYLOAD);
    }

    @Test (expected=IllegalArgumentException.class)
    public void testNullCommitParameter() throws IllegalArgumentException {
        new LatestCommitCheck(null);
    }

    @Test (expected=IllegalArgumentException.class)
    public void testNullCommitPatternParameter() throws IllegalArgumentException {
        commitRegexDefinition.setPattern(null);
        new LatestCommitCheck(commitRegexDefinition);
    }

    @Test
    public void testCheckSimpleRegexMatch() {
        commitRegexDefinition.setPattern(Pattern.compile("Test commit"));
        latestCommitCheck = new LatestCommitCheck(commitRegexDefinition);

        Assert.assertNull("Cannot match valid regex", latestCommitCheck.check(TestUtils.TEST_PAYLOAD));
    }

    @Test
    public void testCheckSimpleRegexNonMatchReturnsExpectedMessage() {
        commitRegexDefinition.setPattern(Pattern.compile("can't.*match.*this"));
        commitRegexDefinition.setMessage("This is commitRegexDefinition message.");
        latestCommitCheck = new LatestCommitCheck(commitRegexDefinition);
        String result = latestCommitCheck.check(TestUtils.TEST_PAYLOAD);

        Assert.assertNotNull("Matched invalid regex", result);
        Assert.assertEquals("Unexpected message returned", "This is commitRegexDefinition message.", result);
    }

    @Test
    public void testCheckSimpleRegexNonMatchReturnsDefaultMessage() {
        commitRegexDefinition.setPattern(Pattern.compile("can't.*match.*this"));
        latestCommitCheck = new LatestCommitCheck(commitRegexDefinition);

        Assert.assertEquals("Unexpected message returned", LatestCommitCheck.DEFAULT_MESSAGE, latestCommitCheck.check(TestUtils.TEST_PAYLOAD));
    }
}