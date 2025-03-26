package org.springframework.samples.gitvision.commit;

import java.util.List;

import org.kohsuke.github.GHRepository;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.auth.payload.response.OkResponse;
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

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/commits")
@Tag(name = "Commit")
public class CommitController {

    private final CommitService commitService;
    private final GVRepoService gvRepoService;

    public CommitController(CommitService commitService, GVRepoService gvRepoService){
        this.commitService = commitService;
        this.gvRepoService = gvRepoService;
    }

    @GetMapping("/{owner}/{repo}")
    public MessageResponse getCommitsByRepository(@PathVariable String owner,
                                                  @PathVariable String repo,
                                                  @RequestParam(defaultValue = "1") Integer page,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetailsImpl ){
        String repositoryName = owner + "/" + repo;
        String login = userDetailsImpl.getUsername();
        List<Commit> commits = this.commitService.getCommitsByRepository(repositoryName, login, page);
        Information information = Information.create("commits", commits)
                                                .put("page", page);
        return OkResponse.of(information);

    }

    @GetMapping("/{owner}/{repo}/{sha}")
    public MessageResponse getCommitByRepositoryNameAndSha(@PathVariable String owner,
                                                  @PathVariable String repo,
                                                  @PathVariable String sha,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception{
            String login = userDetailsImpl.getUsername();
            GHRepository ghRepository = this.gvRepoService.getRepository(owner, repo, login);
            Commit commit = this.commitService.getCommitByRepositoryNameAndSha(ghRepository, sha);
            Information information = Information.create("commit", commit);
            return OkResponse.of(information);
    }

    // @GetMapping("/{owner}/{repo}/byTime")
    // public Map<TimePeriod, Map<Integer, Long>> getNumCommitsGroupByTime(@PathVariable String owner, @PathVariable String repo, @RequestParam String login){
    //     try {
    //         GHRepository ghRepository = this.gvRepoService.getRepository(owner, repo, login);
    //         return this.commitService.getNumCommitsGroupByTime(ghRepository);
    //     } catch (Exception e) {
    //         return null;
    //     }

    // }



}
