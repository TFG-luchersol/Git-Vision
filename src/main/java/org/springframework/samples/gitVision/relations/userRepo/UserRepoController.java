package org.springframework.samples.gitvision.relations.userRepo;

import java.util.Map;
import java.util.List;

import org.kohsuke.github.GHPersonSet;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.user.UserService;
import org.springframework.samples.gitvision.util.Information;
import org.springframework.samples.gitvision.workspace.model.Workspace;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/v1/relation/user_repository")
@Tag(name = "Relation User_Repository")
public class UserRepoController {
    
    @Autowired
    UserRepoService userRepoService;
    
    @Autowired
    UserService userService;

    @GetMapping("/repositories")
    public MessageResponse getAllRepositoriesByUserId(@RequestParam Long userId) {
        Map<String, List<String>> owner_repositories = this.userRepoService.getAllRepositories(userId);
        Information information = Information.create("repositories", owner_repositories);
        return MessageResponse.of(information);
    }

    @GetMapping("/owners")
    public MessageResponse getAllOwnersByUserId(@RequestParam Long userId) {
        List<String> owners = this.userRepoService.getAllOwnersByUserId(userId);
        Information information = Information.create("owners", owners);
        return MessageResponse.of(information);
    }

    @GetMapping("/{owner}/{repo}/contributors")
    public MessageResponse getContributors(@PathVariable String owner, 
                                        @PathVariable String repo, 
                                        @RequestParam String login) {
        try {
            GHRepository ghRepository = this.userRepoService.getRepository(owner, repo, login);
            List<GithubUser> contributors = this.userRepoService.getContributors(ghRepository);
            Information information = Information.create("contributors", contributors);
            return MessageResponse.of(information);
        } catch (Exception e) {
            return MessageResponse.of(e.getMessage());
        }

    }

    @PutMapping("/{owner}/{repo}/token")
    public ResponseEntity<String> updateGithubToken(@PathVariable String owner, 
                                    @PathVariable String repo, 
                                    @RequestParam String login, 
                                    @RequestBody String newGithubToken) {
        try {
            String repositoryName = owner + "/" + repo;
            this.userRepoService.updateGithubToken(repositoryName, login, newGithubToken);
            return ResponseEntity.ok("Token cambiado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        
    }

    @PostMapping
    public void linkUserWithRepository(@RequestBody String login, 
                                       @RequestParam String repo, 
                                       @RequestParam String owner,
                                       @RequestParam(required = false) String token) {
        String repositoryName = owner + "/" + repo; 
        this.userRepoService.linkUserWithRepository(login, repositoryName, token);
    }
    

}


