package org.springframework.samples.gitvision.relations.userWorkspace;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.relations.userWorkspace.model.UserWorkspace;
import org.springframework.samples.gitvision.workspace.model.Workspace;
import org.springframework.stereotype.Service;

@Service
public class UserWorkspaceService {
    
    UserWorkspaceRepository userWorkspaceRepository;

    @Autowired
    public UserWorkspaceService(UserWorkspaceRepository userWorkspaceRepository) {
        this.userWorkspaceRepository = userWorkspaceRepository;
    }

    public List<UserWorkspace> getAllWorkspaceByUserId(Long userId) {
        List<UserWorkspace> userWorkspaces = this.userWorkspaceRepository.findAllUserWorkspacesByUser_Id(userId);
        return userWorkspaces;
    }

}
