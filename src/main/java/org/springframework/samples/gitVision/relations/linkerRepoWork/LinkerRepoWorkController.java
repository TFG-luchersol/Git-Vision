package org.springframework.samples.gitvision.relations.linkerRepoWork;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/linker")
public class LinkerRepoWorkController {

    LinkerRepoWorkService linkerRepoWorkService;

    public LinkerRepoWorkController(LinkerRepoWorkService linkerRepoWorkService) {
        this.linkerRepoWorkService = linkerRepoWorkService;
    }

    @PostMapping("")
    public void linkRepositoryWithWorkspace(@RequestParam String repositoryName, @RequestParam String workspaceId, @RequestParam
     Long userId) {
        this.linkerRepoWorkService.linkRepositoryWithWorkspace(repositoryName, workspaceId, userId);
    }
    
    
}
