package org.springframework.samples.gitvision.user;

import java.util.Objects;

import org.kohsuke.github.GHUser;
import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.model.entity.Person;
import org.springframework.samples.gitvision.util.AESUtil;

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

    @Pattern(regexp = ".+")
    String password;

	@NotBlank
	String githubToken;

	String clockifyToken;

    public boolean hasClockifyToken(){
        return clockifyToken != null;
    }

    public String getDecryptedGithubToken() throws Exception{
        return AESUtil.decrypt(this.githubToken);
    }

    public void setGithubTokenAndEncrypt(String githubToken) throws Exception{
        this.githubToken = AESUtil.encrypt(githubToken);
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
