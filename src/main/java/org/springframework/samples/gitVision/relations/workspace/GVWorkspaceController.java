package org.springframework.samples.gitvision.relations.workspace;

import java.util.List;

import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.auth.payload.response.OkResponse;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.relations.workspace.model.GVWorkspace;
import org.springframework.samples.gitvision.util.Information;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/relation/workspace")
@Tag(name = "Relation workspace")
@SecurityRequirement(name = "bearerAuth")
public class GVWorkspaceController {

    GVWorkspaceService gvWorkspaceService;

    public GVWorkspaceController(GVWorkspaceService gvRepoService) {
        this.gvWorkspaceService = gvRepoService;
    }

    @GetMapping("/workspaces")
    public MessageResponse getAllWorkspacesByUserId(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        Long userId = userDetailsImpl.getId();
        List<GVWorkspace> gvWorkspaces = this.gvWorkspaceService.getAllWorkspaceByUserId(userId);
        Information information = Information.create("workspaces", gvWorkspaces);
        return OkResponse.of(information);
    }

    @PostMapping
    public MessageResponse linkUserWithWorkspace(@RequestParam String workspaceId,
                                                 @RequestParam String name,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
        Long userId = userDetailsImpl.getId();
        gvWorkspaceService.linkUserWithWorkspace(workspaceId, name, userId);
        return OkResponse.of("Workspace creado");
    }

    @GetMapping("/{workspaceId}/timeByUser")
    public MessageResponse getTimeByUser(@RequestParam String workspaceId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        var x = gvWorkspaceService.getTimeByUser(workspaceId, userDetailsImpl.getId());
        Information information = Information.create("x", x);
        return OkResponse.of(information);
    }


}
