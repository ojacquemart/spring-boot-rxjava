package com.ojacquemart.reactive.github.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Repository {
    private String name;
    @JsonProperty("html_url")
    private String url;
}
