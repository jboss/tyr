package org.xstefank.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xstefank.model.Utils;

import static org.mockito.Matchers.anyString;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GitHubAPI.class, Utils.class})
public class GitHubAPITest {

    @Before
    public void before() throws Exception {
        PowerMockito.mockStatic(Utils.class);
        PowerMockito.when(Utils.readTokenFromProperties(anyString(), anyString())).thenReturn(null);

        PowerMockito.spy(GitHubAPI.class);
        PowerMockito.doReturn(null).when(GitHubAPI.class, "readToken");
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateCommitStatusNullParameters() {
        GitHubAPI.updateCommitStatus(null, null, null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetJsonWithCommitsNullParameter() {
        GitHubAPI.getJsonWithCommits(null);
    }
}
