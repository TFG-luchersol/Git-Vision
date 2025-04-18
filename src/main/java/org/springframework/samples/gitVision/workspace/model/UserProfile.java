package org.springframework.samples.gitvision.workspace.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class UserProfile {

    @Column(name = "user_profile_id")
    private String id;

    @Column(name = "user_profile_email")
    private String email;

    @Column(name = "user_profile_name")
    private String name;

}
