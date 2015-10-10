package com.ojacquemart.reactive.github.service;

import com.ojacquemart.reactive.github.domain.RawUser;
import com.ojacquemart.reactive.github.domain.Repository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RestClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RestClient restClient;

    private String baseUrl  = format(RestClient.API_GITHUB_USERS, "foo");

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(restTemplate.getForObject(baseUrl, RawUser.class))
                .thenReturn(new RawUser("foo", "foo bar", "http://foo.bar"));
        when(restTemplate.getForObject(baseUrl + "/followers", RawUser[].class))
                .thenReturn(new RawUser[]{new RawUser("bar", "???", "???")});
        when(restTemplate.getForObject(baseUrl + "/repos", Repository[].class))
                .thenReturn(new Repository[]{new Repository("foo repo", "http://foo.bar/repo")});
    }

    @Test
    public void testGetUser() throws Exception {
        RawUser user = restClient.getUser("foo");

        assertThat(user).isNotNull();

        verify(restTemplate).getForObject(baseUrl, RawUser.class);
    }

    @Test
    public void testGetFollowers() throws Exception {
        RawUser[] followers = restClient.getFollowers("foo");

        assertThat(followers).isNotEmpty();

        verify(restTemplate).getForObject(baseUrl + "/followers", RawUser[].class);
    }

    @Test
    public void testGetRepositories() throws Exception {
        Repository[] repos = restClient.getRepositories("foo");

        assertThat(repos).isNotEmpty();

        verify(restTemplate).getForObject(baseUrl + "/repos", Repository[].class);
    }
}