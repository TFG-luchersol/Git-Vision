package org.springframework.samples.gitvision.relations.userWorkspace;

import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.relations.userWorkspace.model.UserWorkspace;
import org.springframework.samples.gitvision.util.Information;
import org.springframework.samples.gitvision.workspace.model.Workspace;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/relation/user_workspace")
@Tag(name = "Relation User_Workspace")
public class UserWorkspaceController {
    
    UserWorkspaceService userWorkspaceService;

    @Autowired
    public UserWorkspaceController(UserWorkspaceService userRepoService) {
        this.userWorkspaceService = userRepoService;
    }

    @GetMapping("/workspaces")
    public MessageResponse getAllWorkspacesByUserId(@RequestParam Long userId) {
        List<UserWorkspace> userWorkspaces = this.userWorkspaceService.getAllWorkspaceByUserId(userId);
        Information information = Information.create("workspaces", userWorkspaces);
        return MessageResponse.of(information);
    }

    @PostMapping
    public void linkUserWithWorkspace(@RequestBody String login,
                                      @RequestParam String workspaceId, 
                                      @RequestParam String name){
        try {
            userWorkspaceService.linkUserWithWorkspace(workspaceId, name, login);
        } catch (Exception e) {
            // TODO: handle exception
        }
         
    }

}
