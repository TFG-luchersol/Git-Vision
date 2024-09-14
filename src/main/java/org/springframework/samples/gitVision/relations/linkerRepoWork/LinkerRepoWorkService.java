package org.springframework.samples.gitvision.relations.linkerRepoWork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.exceptions.ResourceNotFoundException;
import org.springframework.samples.gitvision.relations.linkerRepoWork.model.LinkerRepoWork;
import org.springframework.samples.gitvision.repository.RepoRepository;
import org.springframework.samples.gitvision.repository.model.Repository;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.user.UserRepository;
import org.springframework.samples.gitvision.workspace.WorkspaceRepository;
import org.springframework.samples.gitvision.workspace.model.Workspace;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LinkerRepoWorkService {
    
    private RepoRepository repoRepository;
    private WorkspaceRepository workspaceRepository;
    private UserRepository userRepository;
    private LinkerRepoWorkRepository linkerRepoWorkRepository;

    @Autowired
    public LinkerRepoWorkService(RepoRepository repoRepository, WorkspaceRepository workspaceRepository,
            UserRepository userRepository, LinkerRepoWorkRepository linkerRepoWorkRepository) {
        this.repoRepository = repoRepository;
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
        this.linkerRepoWorkRepository = linkerRepoWorkRepository;
    }

    @Transactional
    public void linkRepositoryWithWorkspace(String repositoryName, String workspaceId, Long userId){
        Repository repository = repoRepository.findByName(repositoryName).orElseThrow(() -> new ResourceNotFoundException("Repository", "ID", repositoryName));
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new ResourceNotFoundException("Workspace", "ID", workspaceId));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));
        LinkerRepoWork linkerRepoWork = new LinkerRepoWork();
        linkerRepoWork.setRepository(repository);
        linkerRepoWork.setWorkspace(workspace);
        linkerRepoWork.setUser(user);
        linkerRepoWorkRepository.save(linkerRepoWork);
    }
}
