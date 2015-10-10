package com.ojacquemart.reactive.github.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GithubUser {
    private RawUser user;
    private List<RawUser> followers;
    private List<Repository> repositories;
}
