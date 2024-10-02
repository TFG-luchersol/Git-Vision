package org.springframework.samples.gitvision.user;

import java.util.Objects;

import org.springframework.samples.gitvision.model.entity.Person;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends Person {

    // @NotBlank(message = "Password musn't be blank")
    // @Pattern(regexp = ".*")
    String password;

	@NotBlank
	String githubToken;

	String clockifyToken;

	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
		User other = (User) obj;
        return this.id != null && Objects.equals(other.id, this.id);
    }

}
