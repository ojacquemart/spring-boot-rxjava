package com.ojacquemart.reactive.github.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Repository {
    private String name;
    @JsonProperty("html_url")
    private String url;
}
