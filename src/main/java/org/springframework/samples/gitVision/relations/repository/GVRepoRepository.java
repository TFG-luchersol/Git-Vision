package org.springframework.samples.gitvision.relations.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.samples.gitvision.relations.repository.model.GVRepo;
import org.springframework.stereotype.Repository;

@Repository
public interface GVRepoRepository extends RepositoryIdLong<GVRepo> {

    @Query("SELECT r.name FROM GVRepo r WHERE r.user.id = :userId")
    List<String> findNamesByUser_Id(Long userId);

    boolean existsByNameAndUser_Id(String repositoryName, Long userId);

    Optional<GVRepo> findByNameAndUser_Id(String repositoryName, Long userId);

    Optional<GVRepo> findByNameAndUser_Username(String repositoryName, String username);

}
