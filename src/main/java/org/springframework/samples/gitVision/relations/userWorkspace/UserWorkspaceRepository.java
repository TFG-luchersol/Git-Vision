package org.springframework.samples.gitvision.relations.userWorkspace;

import java.util.List;
import java.util.Optional;

import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.samples.gitvision.relations.userWorkspace.model.UserWorkspace;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWorkspaceRepository extends RepositoryIdLong<UserWorkspace> {
    
    List<UserWorkspace> findAllUserWorkspacesByUser_Id(Long userId);

    boolean  existsByNameAndUser_Id(String name, Long userId);

    Optional<UserWorkspace> findByNameAndUser_Id(String repositoryName, Long userId);
    
}
