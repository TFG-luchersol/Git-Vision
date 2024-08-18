package org.springframework.samples.gitvision.model.entity;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class Person extends EntityIdLong {
 
    @Column(unique = true)
	String username;
	
	@Email
	String email;

    @URL
	String avatarUrl;

}
