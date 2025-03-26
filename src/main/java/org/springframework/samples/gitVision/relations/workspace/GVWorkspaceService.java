package org.springframework.samples.gitvision.relations.workspace;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.samples.gitvision.relations.workspace.model.GVWorkspace;
import org.springframework.samples.gitvision.user.GVUser;
import org.springframework.samples.gitvision.user.GVUserRepository;
import org.springframework.samples.gitvision.util.ClockifyApi;
import org.springframework.samples.gitvision.workspace.model.TimeEntry;
import org.springframework.samples.gitvision.workspace.model.UserProfile;
import org.springframework.stereotype.Service;

@Service
public class GVWorkspaceService {

    GVWorkspaceRepository gvWorkspaceRepository;
    GVUserRepository gvUserRepository;

    public GVWorkspaceService(GVWorkspaceRepository gvWorkspaceRepository, GVUserRepository gvUserRepository){
        this.gvWorkspaceRepository = gvWorkspaceRepository;
        this.gvUserRepository = gvUserRepository;
    }


    public List<GVWorkspace> getAllWorkspaceByUserId(Long userId) {
        List<GVWorkspace> gvWorkspaces = this.gvWorkspaceRepository.findAllByUser_Id(userId);
        return gvWorkspaces;
    }

    public Map<String, Long> getTimeByUser(String workspaceId, Long userId) {
        String token = this.gvUserRepository.findById(userId).orElseThrow().getClockifyToken();
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

    public void linkUserWithWorkspace(String workspaceId, String name, Long userId){
        GVUser user = gvUserRepository.findById(userId).get();
        String token = user.getClockifyToken();
        ClockifyApi.getWorkspace(workspaceId, token);
        GVWorkspace gvWorkspace = new GVWorkspace();
        gvWorkspace.setName(name);
        gvWorkspace.setUser(user);
        gvWorkspace.setWorkspaceId(workspaceId);
        gvWorkspaceRepository.save(gvWorkspace);
    }

}
