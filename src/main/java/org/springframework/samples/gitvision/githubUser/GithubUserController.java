package org.springframework.samples.gitvision.githubUser;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
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

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/github_users")
@Tag(name = "Github User")
@SecurityRequirement(name = "bearerAuth")
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
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer limit,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        String repositoryName = owner + "/" + repo;
        String login = userDetailsImpl.getUsername();
        GHRepository ghRepository = gvRepoService.getRepository(repositoryName, login);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        LocalDate d1 = (startDate == null) ? null : OffsetDateTime.parse(startDate, formatter).toLocalDate();
        LocalDate d2 = (endDate == null) ? null : OffsetDateTime.parse(endDate, formatter).toLocalDate();
        var changesInFiles = fileService.getChangesInFilesByUser(ghRepository, author, d1, d2, limit);
        return ResponseEntity.ok(changesInFiles);
    }

}
