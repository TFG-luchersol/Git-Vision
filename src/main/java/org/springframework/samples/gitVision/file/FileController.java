package org.springframework.samples.gitvision.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.file.model.PercentageLanguages;
import org.springframework.samples.gitvision.file.model.TreeFiles.TreeNode;
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

    @GetMapping("/repository/{owner}/{repo}")
    public MessageResponse getFileTreeByRepositoryId(@PathVariable String owner, 
                                                     @PathVariable String repo,
                                                     @RequestParam String login){
        try {
            TreeNode treeNode = this.fileService.getFileTreeByRepositoryName(owner + "/" + repo, login);
            Information information = Information.create("tree", treeNode);
            return MessageResponse.of(information);
        } catch (Exception e) {
            return MessageResponse.of(e.getMessage());
        }
        
    }

    @GetMapping("/languajes/repository/{owner}/{repo}")
    public MessageResponse getPercentageExtensionsByRespositoryId(@PathVariable String owner, 
                                                                  @PathVariable String repo,
                                                                  @RequestParam String login){
        try {
            PercentageLanguages percentageLanguages = this.fileService.getPercentageExtensionsByRespositoryName(owner + "/" + repo, login);
            Information information = Information.create("percentageLanguages", percentageLanguages);
            return MessageResponse.of(information);
        } catch (Exception e) {
            return MessageResponse.of(e.getMessage());
        }
        
    }
    
}
