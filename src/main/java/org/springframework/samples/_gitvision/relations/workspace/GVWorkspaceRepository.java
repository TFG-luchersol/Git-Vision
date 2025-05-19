package org.springframework.samples.gitvision.relations.workspace;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.samples.gitvision.relations.workspace.model.GVWorkspace;
import org.springframework.stereotype.Repository;

@Repository
public interface GVWorkspaceRepository extends RepositoryIdLong<GVWorkspace> {

    List<GVWorkspace> findAllByUser_Id(Long userId);

    @Query("""
        SELECT w FROM GVWorkspace w
        LEFT JOIN GVRepo r ON r.workspace = w AND r.user.id = :userId
        WHERE w.user.id = :userId AND r.id IS NULL
    """)
    List<GVWorkspace> findAllByUser_IdNotLinked(Long userId);

    boolean existsByNameAndUser_Id(String name, Long userId);

    Optional<GVWorkspace> findByNameAndUser_Id(String workspaceName, Long userId);

}
