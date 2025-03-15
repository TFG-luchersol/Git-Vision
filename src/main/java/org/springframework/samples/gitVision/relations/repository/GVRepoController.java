package org.springframework.samples.gitvision.relations.repository;

import java.util.List;
import java.util.Map;

import org.kohsuke.github.GHRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.auth.payload.response.BadResponse;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.auth.payload.response.OkResponse;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.user.GVUserService;
import org.springframework.samples.gitvision.util.Information;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/relation/repository")
@Tag(name = "Relation repository")
public class GVRepoController {
    
    @Autowired
    GVRepoService gvRepoService;
    
    @Autowired
    GVUserService userService;

    @GetMapping
    public MessageResponse getAllRepositoriesByUserId(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        try {
            Long userId = userDetailsImpl.getId();
            Map<String, List<String>> owner_repositories = this.gvRepoService.getAllRepositories(userId);
            Information information = Information.create("repositories", owner_repositories);
            return OkResponse.of(information);
        } catch (Exception e) {
            return BadResponse.of(e);
        }

    }

    @GetMapping("/owners")
    public MessageResponse getAllOwnersByUserId(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        Long userId = userDetailsImpl.getId();
        List<String> owners = this.gvRepoService.getAllOwnersByUserId(userId);
        Information information = Information.create("owners", owners);
        return OkResponse.of(information);
    }

    @GetMapping("/{owner}/{repo}/contributors")
    public MessageResponse getContributors(@PathVariable String owner, 
                                           @PathVariable String repo,
                                           @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        try {
            String login = userDetailsImpl.getUsername();
            GHRepository ghRepository = this.gvRepoService.getRepository(owner, repo, login);
            List<GithubUser> contributors = this.gvRepoService.getContributors(ghRepository);
            Information information = Information.create("contributors", contributors);
            return OkResponse.of(information);
        } catch (Exception e) {
            return BadResponse.of(e);
        }

    }

    @PutMapping("/{owner}/{repo}/token")
    public MessageResponse updateGithubToken(@PathVariable String owner, 
                                    @PathVariable String repo,
                                    @RequestBody String newGithubToken,
                                    @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        try {
            String login = userDetailsImpl.getUsername();
            String repositoryName = owner + "/" + repo;
            this.gvRepoService.updateGithubToken(repositoryName, login, newGithubToken);
            return OkResponse.of("Token cambiado");
        } catch (Exception e) {
            return BadResponse.of(e);
        }
        
    }

    @PostMapping("/{owner}/{repo}")
    public void linkUserWithRepository(@PathVariable String repo, 
                                       @PathVariable String owner,
                                       @RequestParam(required = false) String token,
                                       @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        String login = userDetailsImpl.getUsername();
        String repositoryName = owner + "/" + repo; 
        this.gvRepoService.linkUserWithRepository(login, repositoryName, token);
    }

    @PostMapping
    public MessageResponse linkRepositoryWithWorkspace(@RequestParam String repositoryName, 
                                            @RequestParam String workspaceName, 
                                            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        try {
            Long userId = userDetailsImpl.getId();
            this.gvRepoService.linkRepositoryWithWorkspace(repositoryName, workspaceName, userId);
            return OkResponse.of("Repositorio "+ repositoryName + " enlazado con Workspace" + workspaceName);
        } catch (Exception e) {
            return BadResponse.of(e);
        }
    }
    

}


