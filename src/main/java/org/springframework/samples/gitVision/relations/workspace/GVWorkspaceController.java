package org.springframework.samples.gitvision.relations.workspace;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.relations.workspace.model.GVWorkspace;
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
    public ResponseEntity<List<GVWorkspace>> getAllWorkspacesByUserId(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        Long userId = userDetailsImpl.getId();
        List<GVWorkspace> gvWorkspaces = this.gvWorkspaceService.getAllWorkspaceByUserId(userId);
        return ResponseEntity.ok(gvWorkspaces);
    }

    @PostMapping
    public ResponseEntity<String> linkUserWithWorkspace(@RequestParam String workspaceId,
                                                 @RequestParam String name,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
        Long userId = userDetailsImpl.getId();
        gvWorkspaceService.linkUserWithWorkspace(workspaceId, name, userId);
        return ResponseEntity.ok("Workspace creado");
    }

    @GetMapping("/{workspaceId}/timeByUser")
    public ResponseEntity<Map<String, Long>> getTimeByUser(@RequestParam String workspaceId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        Map<String, Long> timeByUser = gvWorkspaceService.getTimeByUser(workspaceId, userDetailsImpl.getId());
        return ResponseEntity.ok(timeByUser);
    }


}
