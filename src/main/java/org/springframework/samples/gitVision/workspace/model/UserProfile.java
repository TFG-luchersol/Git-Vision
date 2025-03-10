package org.springframework.samples.gitvision.workspace.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfile {
    private String id;
    private String email;
    private String name;
}