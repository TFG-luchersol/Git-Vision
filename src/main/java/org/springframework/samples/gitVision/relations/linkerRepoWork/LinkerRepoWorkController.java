package org.springframework.samples.gitvision.relations.linkerRepoWork;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/linker")
public class LinkerRepoWorkController {

    @Autowired
    LinkerRepoWorkService linkerRepoWorkService;

    @PostMapping
    public void linkRepositoryWithWorkspace(@RequestParam String repositoryName, 
                                            @RequestParam String workspaceName, 
                                            @RequestParam Long userId) {
        try {
            this.linkerRepoWorkService.linkRepositoryWithWorkspace(repositoryName, workspaceName, userId);
        } catch (Exception e) {
            
        }
    }
    
    
}
