package org.springframework.samples.gitvision.model.entity;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class Person extends EntityIdLong {

    @Column(unique = true)
    @NotBlank(message = "model.entity.person.username.not_blank")
	protected String username;

    @Email(message = "{model.entity.person.email.email}")
    private String email;

    @URL(message = "{model.entity.person.avatarUrl.url}")
    private String avatarUrl;


}
