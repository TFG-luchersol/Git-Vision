package org.springframework.samples.gitvision.relations.userWorkspace;

import java.util.List;

import org.springframework.samples.gitvision.auth.payload.response.BadResponse;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.auth.payload.response.OkResponse;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.relations.userWorkspace.model.UserWorkspace;
import org.springframework.samples.gitvision.util.Information;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/relation/user_workspace")
@Tag(name = "Relation User_Workspace")
@SecurityRequirement(name = "bearerAuth")
public class UserWorkspaceController {
    
    UserWorkspaceService userWorkspaceService;

    public UserWorkspaceController(UserWorkspaceService userRepoService) {
        this.userWorkspaceService = userRepoService;
    }

    @GetMapping("/workspaces")
    public MessageResponse getAllWorkspacesByUserId(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        Long userId = userDetailsImpl.getId();
        List<UserWorkspace> userWorkspaces = this.userWorkspaceService.getAllWorkspaceByUserId(userId);
        Information information = Information.create("workspaces", userWorkspaces);
        return OkResponse.of(information);
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

    @GetMapping("/{workspaceId}/timeByUser")
    public MessageResponse getTimeByUser(@RequestParam String workspaceId, 
                                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        try {
            var x = userWorkspaceService.getTimeByUser(workspaceId, userDetailsImpl.getId());
            Information information = Information.create("x", x);
            return OkResponse.of(information);
        } catch (Exception e) {
            return BadResponse.of(e);
        }
    }
    

}
