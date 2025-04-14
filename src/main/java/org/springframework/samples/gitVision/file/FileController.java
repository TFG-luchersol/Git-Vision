package org.springframework.samples.gitvision.file;

import java.util.Map;

import org.kohsuke.github.GHRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.file.model.PercentageLanguages;
import org.springframework.samples.gitvision.file.model.TreeFiles.TreeNode;
import org.springframework.samples.gitvision.relations.repository.GVRepoService;
import org.springframework.samples.gitvision.util.Information;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;
    private final GVRepoService gvRepoService;

    public FileController(FileService fileService, GVRepoService gvRepoService){
        this.fileService = fileService;
        this.gvRepoService = gvRepoService;
    }

    @GetMapping("/repository/{owner}/{repo}")
    public ResponseEntity<?> getFileTreeByRepositoryId(@PathVariable String owner,
                                                     @PathVariable String repo,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception{
        String login = userDetailsImpl.getUsername();
        GHRepository ghRepository = this.gvRepoService.getRepository(owner, repo, login);
        TreeNode treeNode = this.fileService.getFileTreeByRepositoryName(ghRepository);
        Information information = Information.create("tree", treeNode);
        return ResponseEntity.ok(information);
    }

    @GetMapping("/repository/{owner}/{repo}/blob/content")
    public ResponseEntity<byte[]> getFileByRepositoryId(@PathVariable String owner,
                                                 @PathVariable String repo,
                                                 @RequestParam String path,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception{
        String login = userDetailsImpl.getUsername();
        GHRepository ghRepository = this.gvRepoService.getRepository(owner, repo, login);
        byte[] content = this.fileService.getFileContentTreeByPath(ghRepository, path);
        return ResponseEntity.ok(content);
    }

    @GetMapping("/repository/{owner}/{repo}/tree/files")
    public ResponseEntity<TreeNode> getFolderFilesByRepositoryId(@PathVariable String owner,
                                                     @PathVariable String repo,
                                                     @RequestParam String path,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception{
        String login = userDetailsImpl.getUsername();
        GHRepository ghRepository = this.gvRepoService.getRepository(owner, repo, login);
        TreeNode treeNode = this.fileService.getFileSubTreeByRepositoryName(ghRepository, path);
        return ResponseEntity.ok(treeNode);
    }

    @GetMapping("/repository/{owner}/{repo}/extension_counter")
    public ResponseEntity<Map<String, Long>> getExtensionCounterByRepositoryId(@PathVariable String owner,
                                                             @PathVariable String repo,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception{
        String login = userDetailsImpl.getUsername();
        GHRepository ghRepository = this.gvRepoService.getRepository(owner, repo, login);
        Map<String, Long> extensionCounter = this.fileService.getExtensionCounterByRepositoryId(ghRepository);
        return ResponseEntity.ok(extensionCounter);
    }

    @GetMapping("/languajes/repository/{owner}/{repo}")
    public ResponseEntity<PercentageLanguages> getPercentageExtensionsByRespositoryId(@PathVariable String owner,
                                                                  @PathVariable String repo,
                                                                  @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception{
        String login = userDetailsImpl.getUsername();
        GHRepository ghRepository = this.gvRepoService.getRepository(owner, repo, login);
        PercentageLanguages percentageLanguages = this.fileService.getPercentageExtensionsByRespositoryName(ghRepository);
        return ResponseEntity.ok(percentageLanguages);
    }

}
