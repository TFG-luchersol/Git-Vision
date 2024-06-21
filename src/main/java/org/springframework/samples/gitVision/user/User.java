package org.springframework.samples.gitvision.user;

import org.springframework.samples.gitvision.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

	@Column(unique = true)
	String username;
	
	String email;

	String avatarUrl;

	@NotBlank
	String githubToken;

	String clockifyToken;

}
