package org.springframework.samples.gitvision.relations.userWorkspace;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.relations.userWorkspace.model.UserWorkspace;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.user.UserRepository;
import org.springframework.samples.gitvision.util.ClockifyApi;
import org.springframework.stereotype.Service;

@Service
public class UserWorkspaceService {
    
    @Autowired
    UserWorkspaceRepository userWorkspaceRepository;

    @Autowired
    UserRepository userRepository;


    public List<UserWorkspace> getAllWorkspaceByUserId(Long userId) {
        List<UserWorkspace> userWorkspaces = this.userWorkspaceRepository.findAllUserWorkspacesByUser_Id(userId);
        return userWorkspaces;
    }

    public void linkUserWithWorkspace(String workspaceId, String name, String login){
        User user = userRepository.findByUsername(login).get();
        String token = user.getClockifyToken();
        ClockifyApi.getWorkspace(workspaceId, token);
        UserWorkspace userWorkspace = new UserWorkspace();
        userWorkspace.setName(name);
        userWorkspace.setUser(user);
        userWorkspace.setWorkspaceId(workspaceId);
        userWorkspaceRepository.save(userWorkspace);
    }

}
