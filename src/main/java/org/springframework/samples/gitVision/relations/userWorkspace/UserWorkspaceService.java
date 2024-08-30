package org.springframework.samples.gitvision.relations.userWorkspace;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.workspace.model.Workspace;
import org.springframework.stereotype.Service;

@Service
public class UserWorkspaceService {
    
    UserWorkspaceRepository userWorkspaceRepository;

    @Autowired
    public UserWorkspaceService(UserWorkspaceRepository userWorkspaceRepository) {
        this.userWorkspaceRepository = userWorkspaceRepository;
    }

    public List<Workspace> getAllWorkspaceByUserId(Long userId) {
        List<Workspace> workspaces = this.userWorkspaceRepository.findAllWorkspacesByUser_Id(userId);
        return workspaces;
    }

}
