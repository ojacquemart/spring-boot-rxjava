package com.ojacquemart.reactive.github.service;

import com.ojacquemart.reactive.github.domain.Repository;
import com.ojacquemart.reactive.github.domain.RawUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;

@Slf4j
public class RestClient {

    public static final String API_GITHUB_USERS = "https://api.github.com/users/%s";
    public static final String API_GITHUB_FOLLOWERS = API_GITHUB_USERS + "/followers";
    public static final String API_GITHUB_REPOS = API_GITHUB_USERS + "/repos";

    private final RestTemplate restTemplate;

    public RestClient() {
        this(new RestTemplate());
    }

    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RawUser getUser(String login) {
        log.info("Get user {}", login);

        return restTemplate.getForObject(format(API_GITHUB_USERS, login), RawUser.class);
    }

    public RawUser[] getFollowers(String login) {
        log.info("Get followers {}", login);

        return restTemplate.getForObject(format(API_GITHUB_FOLLOWERS, login), RawUser[].class);
    }

    public Repository[] getRepositories(String login) {
        log.info("Get repos {}", login);

        return restTemplate.getForObject(format(API_GITHUB_REPOS, login), Repository[].class);
    }

}
