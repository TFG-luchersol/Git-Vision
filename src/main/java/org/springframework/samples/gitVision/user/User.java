package org.springframework.samples.gitvision.user;

import java.util.Objects;

import org.kohsuke.github.GHUser;
import org.springframework.samples.gitvision.model.entity.Person;
import org.springframework.samples.gitvision.util.AESConverter;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends Person {

    @Pattern(regexp = ".+")
    String password;

	@Convert(converter = AESConverter.class)
	String githubToken;

	@Convert(converter = AESConverter.class)
	String clockifyToken;

    public boolean hasClockifyToken(){
        return clockifyToken != null;
    }

    public static User parse(GHUser ghUser) {
        User user = new User();
        user.setAvatarUrl(ghUser.getAvatarUrl());
        user.setUsername(ghUser.getLogin());
        return user;
    }

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
