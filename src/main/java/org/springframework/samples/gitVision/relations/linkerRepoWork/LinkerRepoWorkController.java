package org.springframework.samples.gitvision.relations.linkerRepoWork;

import java.util.List;
import java.util.Map;

import org.springframework.samples.gitvision.auth.payload.response.BadResponse;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.auth.payload.response.OkResponse;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.util.Information;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/linker")
public class LinkerRepoWorkController {

    LinkerRepoWorkService linkerRepoWorkService;

    public LinkerRepoWorkController(LinkerRepoWorkService linkerRepoWorkService){
        this.linkerRepoWorkService = linkerRepoWorkService;
    }

    @PostMapping
    public MessageResponse linkRepositoryWithWorkspace(@RequestParam String repositoryName, 
                                            @RequestParam String workspaceName, 
                                            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        try {
            Long userId = userDetailsImpl.getId();
            this.linkerRepoWorkService.linkRepositoryWithWorkspace(repositoryName, workspaceName, userId);
            return OkResponse.of("Repositorio "+ repositoryName + " enlazado con Workspace" + workspaceName);
        } catch (Exception e) {
            return BadResponse.of(e);
        }
    }

    @GetMapping
    public MessageResponse getAllRelationsByUserId(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        Long userId = userDetailsImpl.getId();
        Map<String, List<String>> workspace_repository = this.linkerRepoWorkService.getAllRelationsByUserId(userId);
        Information information = Information.create("workspace_repository", workspace_repository);
        return OkResponse.of(information);
    }
    
    
}
