package org.springframework.samples.gitvision.relations.workspace;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.relations.repository.GVRepoService;
import org.springframework.samples.gitvision.relations.repository.model.GVRepo;
import org.springframework.samples.gitvision.relations.workspace.model.AliasWorkspaceDTO;
import org.springframework.samples.gitvision.relations.workspace.model.GVWorkspace;
import org.springframework.samples.gitvision.relations.workspace.model.GVWorkspaceUserConfig;
import org.springframework.samples.gitvision.util.MessageResolver;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/relation/workspace")
@Tag(name = "Relation workspace")
@SecurityRequirement(name = "bearerAuth")
public class GVWorkspaceController {

    private final GVWorkspaceService gvWorkspaceService;
    private final GVRepoService gvRepoService;
    private final MessageResolver msg;

    public GVWorkspaceController(GVWorkspaceService gvWorkspaceService, GVRepoService gvRepoService,
            MessageResolver msg) {
        this.gvWorkspaceService = gvWorkspaceService;
        this.gvRepoService = gvRepoService;
        this.msg = msg;
    }

    @GetMapping
    public ResponseEntity<List<GVWorkspace>> getAllWorkspacesByUserId(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        Long userId = userDetailsImpl.getId();
        List<GVWorkspace> gvWorkspaces = this.gvWorkspaceService.getAllWorkspaceByUserId(userId);
        return ResponseEntity.ok(gvWorkspaces);
    }

    @GetMapping("/not_linked")
    public ResponseEntity<List<GVWorkspace>> getAllWorkspacesByUserIdNotLinked(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        Long userId = userDetailsImpl.getId();
        List<GVWorkspace> gvWorkspaces = this.gvWorkspaceService.getAllWorkspaceByUserIdNotLinked(userId);
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

    @GetMapping("/{workspaceName}/user_alias")
    public ResponseEntity<List<GVWorkspaceUserConfig>> getRepositoryConfiguration(
            @PathVariable String workspaceName,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        List<GVWorkspaceUserConfig> gvWorkspaceUserConfigurations = gvWorkspaceService.getWorkspaceConfiguration(workspaceName, userDetailsImpl.getId());
        return ResponseEntity.ok(gvWorkspaceUserConfigurations);
    }

    @PutMapping("/{workspaceName}/user_alias")
    public ResponseEntity<String> updateAliases(
            @PathVariable String workspaceName,
            @RequestBody @Valid AliasWorkspaceDTO aliasDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        var gvWorkspaceUserConfig = gvWorkspaceService.updateAliaUserConfigurations(workspaceName, userDetailsImpl.getId(), aliasDTO);
        String message = msg.get("api.v1.relation.workspace.workspace_name.user_alias.put.response",
                                 gvWorkspaceUserConfig.getUserProfile().getName());
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{workspaceName}/user_alias/refresh")
    public ResponseEntity<List<GVWorkspaceUserConfig>> refreshWorkspaceConfiguration(
            @PathVariable String workspaceName,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        GVRepo gvRepo = this.gvRepoService.getGvRepoByWorkspaceNameAndUser_Username(workspaceName, userDetailsImpl.getUsername());
        List<GVWorkspaceUserConfig> aliases = gvWorkspaceService.refreshWorkspaceConfiguration(gvRepo);
        return ResponseEntity.ok(aliases);
    }


}
