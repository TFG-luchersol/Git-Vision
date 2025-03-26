package org.springframework.samples.gitvision.relations.workspace;

import java.util.List;
import java.util.Optional;

import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.samples.gitvision.relations.workspace.model.GVWorkspace;
import org.springframework.stereotype.Repository;

@Repository
public interface GVWorkspaceRepository extends RepositoryIdLong<GVWorkspace> {

    List<GVWorkspace> findAllByUser_Id(Long userId);

    boolean existsByNameAndUser_Id(String name, Long userId);

    Optional<GVWorkspace> findByNameAndUser_Id(String repositoryName, Long userId);

}
