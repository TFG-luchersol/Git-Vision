package org.springframework.samples.gitvision.relations.linkerRepoWork;

import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.samples.gitvision.relations.linkerRepoWork.model.LinkerRepoWork;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import org.springframework.samples.gitvision.user.User;


@Repository
public interface LinkerRepoWorkRepository extends RepositoryIdLong<LinkerRepoWork> {
    
    boolean existsByRepositoryIdAndWorkspaceIdAndUser(Long repositoryId, String workspaceId, User user);
}