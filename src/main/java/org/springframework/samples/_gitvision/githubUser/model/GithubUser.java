package org.springframework.samples.gitvision.githubUser.model;

import java.io.IOException;
import java.util.Objects;

import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitUser;
import org.kohsuke.github.GHRepository.Contributor;
import org.springframework.samples.gitvision.model.entity.Person;

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

    public static GithubUser parseGitUser(GitUser gitUser) throws IOException{
        GithubUser githubUser = new GithubUser();
        githubUser.setEmail(gitUser.getEmail());
        githubUser.setUsername(gitUser.getName());
        return githubUser;
    }

    public static GithubUser parseContributor(Contributor contributor) {
        GithubUser githubUser = new GithubUser();
        githubUser.setUsername(contributor.getLogin());
        githubUser.setAvatarUrl(contributor.getAvatarUrl());
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
