package org.springframework.samples.gitvision.workspace.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfile {
    private String id;
    private String email;
    private String name;
}