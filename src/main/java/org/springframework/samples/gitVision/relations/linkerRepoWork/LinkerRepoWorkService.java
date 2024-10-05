package org.springframework.samples.gitvision.relations.linkerRepoWork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.exceptions.ResourceNotFoundException;
import org.springframework.samples.gitvision.relations.linkerRepoWork.model.LinkerRepoWork;
import org.springframework.samples.gitvision.relations.userRepo.UserRepoRepository;
import org.springframework.samples.gitvision.relations.userRepo.model.UserRepo;
import org.springframework.samples.gitvision.relations.userWorkspace.UserWorkspaceRepository;
import org.springframework.samples.gitvision.relations.userWorkspace.model.UserWorkspace;
import org.springframework.samples.gitvision.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LinkerRepoWorkService {
    
    private UserRepository userRepository;
    private UserRepoRepository userRepoRepository;
    private UserWorkspaceRepository userWorkspaceRepository;
    private LinkerRepoWorkRepository linkerRepoWorkRepository;
    
    @Autowired
    public LinkerRepoWorkService(UserRepository userRepository, UserRepoRepository userRepoRepository,
            UserWorkspaceRepository userWorkspaceRepository, LinkerRepoWorkRepository linkerRepoWorkRepository) {
        this.userRepository = userRepository;
        this.userRepoRepository = userRepoRepository;
        this.userWorkspaceRepository = userWorkspaceRepository;
        this.linkerRepoWorkRepository = linkerRepoWorkRepository;
    }

    @Transactional
    public void linkRepositoryWithWorkspace(String repositoryName, String workspaceName, Long userId) throws Exception{
        if(!userRepository.existsById(userId))
            throw new ResourceNotFoundException("User", "ID", userId);
        
        UserRepo userRepo = userRepoRepository.findByNameAndUser_Id(repositoryName, userId).orElseThrow(() -> new ResourceNotFoundException("UserRepo not found"));;
        UserWorkspace userWorkspace = userWorkspaceRepository.findByNameAndUser_Id(workspaceName, userId).orElseThrow(() -> new ResourceNotFoundException("UserWorkspace not found"));;
        
        if(linkerRepoWorkRepository.existsByUserRepoAndUserWorkspace(userRepo, userWorkspace)){
            throw new Exception("Relation exist");
        }
        LinkerRepoWork linkerRepoWork = new LinkerRepoWork(userRepo, userWorkspace);
        linkerRepoWorkRepository.save(linkerRepoWork);
    }
}
