package org.springframework.samples.gitvision.repository;

import java.util.Optional;

import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.samples.gitvision.repository.model.Repository;

public interface RepoRepository extends RepositoryIdLong<Repository> {

    Optional<Repository> findByName(String name);
    
}
