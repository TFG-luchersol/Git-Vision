package org.springframework.samples.gitvision.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.file.model.PercentageLanguages;
import org.springframework.samples.gitvision.file.model.TreeFiles.TreeNode;
import org.springframework.samples.gitvision.util.Data;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/files")
public class FileController {
    
    FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/repository/{repositoryId}")
    public MessageResponse getFileTreeByRepositoryId(@PathVariable Long repositoryId){
        try {
            TreeNode treeNode = this.fileService.getFileTreeByRepositoryId(repositoryId);
            Data data = Data.create("tree", treeNode);
            return MessageResponse.of(data);
        } catch (Exception e) {
            return MessageResponse.of(e.getMessage());
        }
        
    }

    @GetMapping("/languajes/repository/{repositoryId}")
    public MessageResponse getPercentageExtensionsByRespositoryId(@PathVariable Long repositoryId){
        try {
            PercentageLanguages percentageLanguages = this.fileService.getPercentageExtensionsByRespositoryId(repositoryId);
            Data data = Data.create("percentageLanguages", percentageLanguages);
            return MessageResponse.of(data);
        } catch (Exception e) {
            return MessageResponse.of(e.getMessage());
        }
        
    }
    
}
