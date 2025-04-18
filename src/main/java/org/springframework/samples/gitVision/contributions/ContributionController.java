package org.springframework.samples.gitvision.contributions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.kohsuke.github.GHRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.contributions.model.Contribution;
import org.springframework.samples.gitvision.relations.repository.GVRepoService;
import org.springframework.samples.gitvision.relations.repository.model.GVRepo;
import org.springframework.samples.gitvision.task.TaskService;
import org.springframework.samples.gitvision.util.Checker;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/contributions")
@Tag(name = "Contributions")
@SecurityRequirement(name = "bearerAuth")
public class ContributionController {

    private final ContributionService contributionService;
    private final GVRepoService gvRepoService;
    private final TaskService taskService;

    public ContributionController(ContributionService contributionService, GVRepoService gvRepoService,
            TaskService taskService){
        this.contributionService = contributionService;
        this.gvRepoService = gvRepoService;
        this.taskService = taskService;
    }

    @GetMapping("/{owner}/{repo}/between_time")
    public ResponseEntity<List<Contribution>> getCommitsByUserBetweenDates(@PathVariable String owner,
                                                        @PathVariable String repo,
                                                        @RequestParam(defaultValue = "false") String isFolder,
                                                        @RequestParam(required = false) String path,
                                                        @RequestParam(required = false) String startDate,
                                                        @RequestParam(required = false) String endDate,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        String repositoryName = owner + "/" + repo;
        String login = userDetailsImpl.getUsername();
        GHRepository ghRepository = this.gvRepoService.getRepository(repositoryName, login);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date d1 = startDate == null ? null : dateFormat.parse(startDate);
        Date d2 = endDate == null ? null : dateFormat.parse(endDate);
        List<Contribution> contributions = isFolder == "" ?
            this.contributionService.getContributionsInFolderByDateBetweenDates(ghRepository, repositoryName, login, path, d1, d2) :
            this.contributionService.getContributionsByDateBetweenDates(repositoryName, login, path, d1, d2);
        return ResponseEntity.ok(contributions);
    }

    @GetMapping("/{owner}/{repo}/time")
    public ResponseEntity<?> time(@PathVariable String owner,
                                @PathVariable String repo,
                                @RequestParam(required = false) Long id,
                                @RequestParam(required = false) String name,
                                @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        Long userId = 93008812L;
        String repositoryName = owner + "/" + repo;
        Checker.checkOrBadRequest((id == null) ^ (name == null),
                          "Debe proporcionar solo un ID o un nombre, no ambos.");
        String taskName = name != null ? name : "#"+id;
        GVRepo gvRepo = gvRepoService.getGvRepoByNameAndUser_Id(repositoryName, userId);

        Map<String, Long> duration = this.taskService.timeByNameGroupByUserId(gvRepo, taskName, userId);
        return ResponseEntity.ok(duration);
    }

}
