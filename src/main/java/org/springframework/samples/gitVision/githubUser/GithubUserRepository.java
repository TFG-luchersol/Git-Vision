package org.springframework.samples.gitvision.githubUser;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;

import java.util.List;
import java.util.Optional;


public interface GithubUserRepository extends RepositoryIdLong<GithubUser> {
    
    Optional<GithubUser> findByUsername(String username);

    // @Query("Select g from GithubUser g where g.username = :username")
    // List<GithubUser> getAllByUsername(String username);
}
