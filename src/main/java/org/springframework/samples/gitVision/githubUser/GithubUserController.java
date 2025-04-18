package org.springframework.samples.gitvision.githubUser;

import java.time.LocalDate;
import java.util.List;

import org.kohsuke.github.GHRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.file.FileService;
import org.springframework.samples.gitvision.file.model.ChangesInFile;
import org.springframework.samples.gitvision.relations.repository.GVRepoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/github_users")
public class GithubUserController {

    private final FileService fileService;
    private final GVRepoService gvRepoService;

    public GithubUserController(FileService fileService, GVRepoService gvRepoService) {
        this.fileService = fileService;
        this.gvRepoService = gvRepoService;
    }

    @GetMapping("/{owner}/{repo}/files")
    public ResponseEntity<List<ChangesInFile>> getChangedFiles(@PathVariable String owner,
            @PathVariable String repo,
            @RequestParam String author,
            @RequestParam(required = false) LocalDate starDate,
            @RequestParam(required = false) LocalDate endDate,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        String repositoryName = owner + "/" + repo;
        String login = userDetailsImpl.getUsername();
        GHRepository ghRepository = gvRepoService.getRepository(repositoryName, login);
        List<ChangesInFile> changesInFiles = fileService.getChangesInFilesByUser(ghRepository, author, starDate, endDate);
        return ResponseEntity.ok(changesInFiles);
    }

}
