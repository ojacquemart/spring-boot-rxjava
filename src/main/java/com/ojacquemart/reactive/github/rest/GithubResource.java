package com.ojacquemart.reactive.github.rest;

import com.ojacquemart.reactive.github.domain.GithubUser;
import com.ojacquemart.reactive.github.service.GithubService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/github/users")
public class GithubResource {

    private GithubService githubService;

    @RequestMapping("/{login}")
    public GithubUser getUser(@PathVariable("login") String login) {
        log.info("Get github.com/{} infos", login);

        return githubService.getUser(login);
    }

}
