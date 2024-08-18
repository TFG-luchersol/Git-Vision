package org.springframework.samples.gitvision.githubUser;

import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import java.util.Optional;


public interface GithubUserRepository extends RepositoryIdLong<GithubUser> {
    
    Optional<GithubUser> findByUsername(String username);
}
