package org.springframework.samples.gitvision.githubUser.model;

import java.io.IOException;
import java.util.Objects;

import org.kohsuke.github.GHUser;
import org.springframework.samples.gitvision.model.entity.Person;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubUser extends Person {

    public static GithubUser parse(GHUser ghUser) throws IOException{
        GithubUser githubUser = new GithubUser();
        githubUser.setAvatarUrl(ghUser.getAvatarUrl());
        githubUser.setEmail(ghUser.getEmail());
        githubUser.setUsername(ghUser.getLogin());
        return githubUser;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GithubUser other = (GithubUser) obj;
        return this.id != null && Objects.equals(other.id, this.id);
    }
    
}
