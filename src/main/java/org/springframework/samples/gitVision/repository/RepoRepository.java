package org.springframework.samples.gitvision.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;

public interface RepoRepository extends RepositoryIdLong<Repository> {

    @Query("select r from Repository r where r.name = :name")
    Repository findByName(String name);
    
}
