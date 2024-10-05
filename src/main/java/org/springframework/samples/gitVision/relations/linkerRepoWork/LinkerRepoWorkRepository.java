package org.springframework.samples.gitvision.relations.linkerRepoWork;

import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.samples.gitvision.relations.linkerRepoWork.model.LinkerRepoWork;
import org.springframework.samples.gitvision.relations.userRepo.model.UserRepo;
import org.springframework.samples.gitvision.relations.userWorkspace.model.UserWorkspace;
import org.springframework.stereotype.Repository;


@Repository
public interface LinkerRepoWorkRepository extends RepositoryIdLong<LinkerRepoWork> {
    
    boolean existsByUserRepoAndUserWorkspace(UserRepo userRepo, UserWorkspace userWorkspace);

}
