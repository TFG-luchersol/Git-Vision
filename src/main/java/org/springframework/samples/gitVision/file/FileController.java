package org.springframework.samples.gitvision.file;

import java.util.Map;

import org.kohsuke.github.GHRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.auth.payload.response.BadResponse;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.auth.payload.response.OkResponse;
import org.springframework.samples.gitvision.file.model.PercentageLanguages;
import org.springframework.samples.gitvision.file.model.TreeFiles.TreeNode;
import org.springframework.samples.gitvision.relations.userRepo.UserRepoService;
import org.springframework.samples.gitvision.util.Information;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/files")
public class FileController {
    
    @Autowired
    FileService fileService;

    @Autowired
    UserRepoService userRepoService;

    @GetMapping("/repository/{owner}/{repo}")
    public MessageResponse getFileTreeByRepositoryId(@PathVariable String owner, 
                                                     @PathVariable String repo,
                                                     @RequestParam String login){
        try {
            GHRepository ghRepository = this.userRepoService.getRepository(owner, repo, login);
            TreeNode treeNode = this.fileService.getFileTreeByRepositoryName(ghRepository);
            Information information = Information.create("tree", treeNode);
            return OkResponse.of(information);
        } catch (Exception e) {
            return BadResponse.of(e);
        }
        
    }

    @GetMapping("/repository/{owner}/{repo}/extension_counter")
    public MessageResponse getExtensionCounterByRepositoryId(@PathVariable String owner, 
                                                             @PathVariable String repo,
                                                             @RequestParam String login){
        try {
            GHRepository ghRepository = this.userRepoService.getRepository(owner, repo, login);
            Map<String, Long> extensionCounter = this.fileService.getExtensionCounterByRepositoryId(ghRepository);
            Information information = Information.create("extensionCounter", extensionCounter);
            return OkResponse.of(information);
        } catch (Exception e) {
            return BadResponse.of(e);
        }
        
    }

    @GetMapping("/languajes/repository/{owner}/{repo}")
    public MessageResponse getPercentageExtensionsByRespositoryId(@PathVariable String owner, 
                                                                  @PathVariable String repo,
                                                                  @RequestParam String login){
        try {
            GHRepository ghRepository = this.userRepoService.getRepository(owner, repo, login);
            PercentageLanguages percentageLanguages = this.fileService.getPercentageExtensionsByRespositoryName(ghRepository);
            Information information = Information.create("percentageLanguages", percentageLanguages);
            return OkResponse.of(information);
        } catch (Exception e) {
            return BadResponse.of(e);
        }
        
    }
    
}
