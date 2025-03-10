package org.springframework.samples.gitvision.relations.userWorkspace;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.samples.gitvision.relations.userWorkspace.model.UserWorkspace;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.user.UserRepository;
import org.springframework.samples.gitvision.util.ClockifyApi;
import org.springframework.samples.gitvision.workspace.model.TimeEntry;
import org.springframework.samples.gitvision.workspace.model.UserProfile;
import org.springframework.stereotype.Service;

@Service
public class UserWorkspaceService {
    
    UserWorkspaceRepository userWorkspaceRepository;
    UserRepository userRepository;

    public UserWorkspaceService(UserWorkspaceRepository userWorkspaceRepository, UserRepository userRepository){
        this.userWorkspaceRepository = userWorkspaceRepository;
        this.userRepository = userRepository;
    }


    public List<UserWorkspace> getAllWorkspaceByUserId(Long userId) {
        List<UserWorkspace> userWorkspaces = this.userWorkspaceRepository.findAllUserWorkspacesByUser_Id(userId);
        return userWorkspaces;
    }

    public Map<String, Long> getTimeByUser(String workspaceId, Long userId) {
        String token = this.userRepository.findById(userId).orElseThrow().getClockifyToken();
        String url = String.format("/v1/workspaces/%s/users", workspaceId);
        List<UserProfile> users = ClockifyApi.getUsers(workspaceId, token);
        Map<String, Long> res = new HashMap<>();
        for (UserProfile user : users) {
            url = String.format("/v1/workspaces/%s/user/%s/time-entries", workspaceId, user.getId());
            TimeEntry[] timeEntries = ClockifyApi.requestClockify(url, token, TimeEntry[].class);
            String name = user.getName();
            Long time = Arrays.stream(timeEntries).mapToLong(entry -> entry.getTimeInterval().getDuration().getNano()).sum();
            res.put(name, time);
        } 
        
        return res;
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
