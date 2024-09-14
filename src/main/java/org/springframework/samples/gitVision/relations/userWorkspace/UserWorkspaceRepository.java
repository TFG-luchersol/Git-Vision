package org.springframework.samples.gitvision.relations.userWorkspace;

import java.util.List;

import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.samples.gitvision.relations.userWorkspace.model.UserWorkspace;
import org.springframework.samples.gitvision.workspace.model.Workspace;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWorkspaceRepository extends RepositoryIdLong<UserWorkspace> {
    
    List<UserWorkspace> findAllUserWorkspacesByUser_Id(Long userId);

}
