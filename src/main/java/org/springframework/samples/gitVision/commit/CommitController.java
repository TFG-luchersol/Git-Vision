package org.springframework.samples.gitvision.commit;

import java.io.IOException;
import java.util.List;

import org.kohsuke.github.GHRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.relations.repository.GVRepoService;
import org.springframework.samples.gitvision.util.Information;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/commits")
@Tag(name = "Commit")
@SecurityRequirement(name = "bearerAuth")
public class CommitController {

    private final CommitService commitService;
    private final GVRepoService gvRepoService;

    public CommitController(CommitService commitService, GVRepoService gvRepoService){
        this.commitService = commitService;
        this.gvRepoService = gvRepoService;
    }

    @GetMapping("/{owner}/{repo}")
    public ResponseEntity<Information> getCommitsByRepository(@PathVariable String owner,
                                                  @PathVariable String repo,
                                                  @RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(required = false) String filter,
                                                  @RequestParam(defaultValue = "false") boolean isRegex,
                                                  @RequestParam(defaultValue = "false") boolean isOwner,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetailsImpl ) throws IOException{
        String repositoryName = owner + "/" + repo;
        String login = userDetailsImpl.getUsername();
        List<Commit> commits = filter != null && !filter.isBlank() ?
            this.commitService.getCommitsByRepositoryWithFilter(repositoryName, login, filter, isRegex, isOwner) :
            this.commitService.getCommitsByRepository(repositoryName, login, page);
        Information information = Information.create("commits", commits)
                                                .put("page", page);
        return ResponseEntity.ok(information);

    }

    @GetMapping("/{owner}/{repo}/{sha}")
    public ResponseEntity<Commit> getCommitByRepositoryNameAndSha(@PathVariable String owner,
                                                  @PathVariable String repo,
                                                  @PathVariable String sha,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception{
        String login = userDetailsImpl.getUsername();
        GHRepository ghRepository = this.gvRepoService.getRepository(owner, repo, login);
        Commit commit = this.commitService.getCommitByRepositoryNameAndSha(ghRepository, sha);
        return ResponseEntity.ok(commit);
    }

}
