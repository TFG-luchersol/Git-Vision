package org.springframework.samples.gitvision.user;

import org.springframework.samples.gitvision.model.entity.Person;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends Person {

	@NotBlank
	String githubToken;

	String clockifyToken;

}
