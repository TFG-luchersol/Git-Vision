package org.springframework.samples.gitvision.relations.linkerRepoWork;

import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.exceptions.ResourceNotFoundException;
import org.springframework.samples.gitvision.relations.linkerRepoWork.model.LinkerRepoWork;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.user.UserRepository;
import org.springframework.samples.gitvision.util.ClockifyApi;
import org.springframework.samples.gitvision.workspace.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LinkerRepoWorkService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LinkerRepoWorkRepository linkerRepoWorkRepository;

    @Transactional
    public void linkRepositoryWithWorkspace(Long repositoryId, String workspaceId, Long userId) throws Exception{
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));
        if (linkerRepoWorkRepository.existsByRepository_idAndWorkspace_idAndUser(repositoryId, workspaceId, user)) 
            throw new Exception();
        try {
            GitHub.connect().getRepositoryById(repositoryId);
        } catch (Exception e) {
            throw new Exception();
        }
        try {
            ClockifyApi.getWorkspace(workspaceId, user.getClockifyToken());
        } catch (Exception e) {
            throw new Exception();
        }
        LinkerRepoWork linkerRepoWork = new LinkerRepoWork(repositoryId, workspaceId, user);
        linkerRepoWorkRepository.save(linkerRepoWork);
    }
}
