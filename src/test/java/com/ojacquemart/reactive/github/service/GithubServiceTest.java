package com.ojacquemart.reactive.github.service;

import com.ojacquemart.reactive.github.domain.GithubUser;
import com.ojacquemart.reactive.github.domain.RawUser;
import com.ojacquemart.reactive.github.domain.Repository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.util.StopWatch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GithubServiceTest {

    @Mock
    private RestClient restClient;
    @InjectMocks
    private GithubService githubService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(restClient.getUser(anyString())).thenAnswer(m -> getUserWithSleepTime());
        when(restClient.getFollowers(anyString())).thenAnswer(m -> getFollowersWithSleepTime());
        when(restClient.getRepositories(anyString())).then(m -> getReposWithSleepTime());
    }

    @Test
    public void testGetUser() throws Exception {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();

        GithubUser foo = githubService.getUser("foo");
        stopwatch.stop();

        long duration = stopwatch.getTotalTimeMillis();

        assertThat(foo).isNotNull();

        assertThat(foo.getUser()).isNotNull();
        assertThat(foo.getUser().getLogin()).isEqualTo("foo");
        assertThat(foo.getUser().getName()).isEqualTo("foo bar");

        assertThat(foo.getFollowers())
                .hasSize(3)
                .extracting("name")
                .contains("bar", "qix", "baz");
        assertThat(foo.getRepositories())
                .hasSize(2)
                .extracting("name")
                .contains("foo", "bar");

        // duration should be at least 300ms & max 400ms with overhead
        assertThat(duration).isGreaterThanOrEqualTo(300);
        assertThat(duration).isLessThanOrEqualTo(400);
    }

    // mock rest client methods
    // - user sleep 200ms
    // - followers & repos sleep for 300ms
    // overall sleep time should be ~ 300ms max

    public RawUser getUserWithSleepTime() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }

        return new RawUser("foo", "foo bar", "???");
    }

    public RawUser[] getFollowersWithSleepTime() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
        }

        return new RawUser[]{new RawUser("foo", "bar", null), new RawUser("foo", "qix", null), new RawUser("foo", "baz", null)};
    }

    public Repository[] getReposWithSleepTime() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
        }

        return new Repository[]{new Repository("foo", "???"), new Repository("bar", "???")};
    }
}