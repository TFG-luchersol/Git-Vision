package org.springframework.samples.gitvision.file;

import java.util.Map;

import org.kohsuke.github.GHRepository;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.auth.payload.response.OkResponse;
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
    public MessageResponse getFileTreeByRepositoryId(@PathVariable String owner,
                                                     @PathVariable String repo,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception{
        String login = userDetailsImpl.getUsername();
        GHRepository ghRepository = this.gvRepoService.getRepository(owner, repo, login);
        TreeNode treeNode = this.fileService.getFileTreeByRepositoryName(ghRepository);
        Information information = Information.create("tree", treeNode);
        return OkResponse.of(information);
    }

    @GetMapping("/repository/{owner}/{repo}/blob/content")
    public MessageResponse getFileByRepositoryId(@PathVariable String owner,
                                                 @PathVariable String repo,
                                                 @RequestParam String path,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception{
        String login = userDetailsImpl.getUsername();
        GHRepository ghRepository = this.gvRepoService.getRepository(owner, repo, login);
        byte[] content = this.fileService.getFileContentTreeByPath(ghRepository, path);
        Information information = Information.create("content", content);
        return OkResponse.of(information);
    }

    @GetMapping("/repository/{owner}/{repo}/tree/files")
    public MessageResponse getFolderFilesByRepositoryId(@PathVariable String owner,
                                                     @PathVariable String repo,
                                                     @RequestParam String path,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception{
        String login = userDetailsImpl.getUsername();
        GHRepository ghRepository = this.gvRepoService.getRepository(owner, repo, login);
        TreeNode treeNode = this.fileService.getFileSubTreeByRepositoryName(ghRepository, path);
        Information information = Information.create("tree", treeNode);
        return OkResponse.of(information);
    }

    @GetMapping("/repository/{owner}/{repo}/extension_counter")
    public MessageResponse getExtensionCounterByRepositoryId(@PathVariable String owner,
                                                             @PathVariable String repo,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception{
        String login = userDetailsImpl.getUsername();
        GHRepository ghRepository = this.gvRepoService.getRepository(owner, repo, login);
        Map<String, Long> extensionCounter = this.fileService.getExtensionCounterByRepositoryId(ghRepository);
        Information information = Information.create("extensionCounter", extensionCounter);
        return OkResponse.of(information);
    }

    @GetMapping("/languajes/repository/{owner}/{repo}")
    public MessageResponse getPercentageExtensionsByRespositoryId(@PathVariable String owner,
                                                                  @PathVariable String repo,
                                                                  @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception{
        String login = userDetailsImpl.getUsername();
        GHRepository ghRepository = this.gvRepoService.getRepository(owner, repo, login);
        PercentageLanguages percentageLanguages = this.fileService.getPercentageExtensionsByRespositoryName(ghRepository);
        Information information = Information.create("percentageLanguages", percentageLanguages);
        return OkResponse.of(information);
    }

}
