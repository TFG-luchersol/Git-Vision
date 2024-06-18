package org.springframework.samples.gitVision.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import org.springframework.samples.gitVision.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

	@Column(unique = true)
	String username;

	@NotBlank
	String githubToken;

	String clockifyToken;
	
	String email;

	String avatarUrl;

}
