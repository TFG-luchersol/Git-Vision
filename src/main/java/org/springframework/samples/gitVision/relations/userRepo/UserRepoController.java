package org.springframework.samples.gitvision.relations.userRepo;

import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.relations.userRepo.model.UserRepo;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.user.UserService;
import org.springframework.samples.gitvision.util.Credential;
import org.springframework.samples.gitvision.util.Information;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

    @PostMapping
    public void linkUserWithRepository(@RequestBody Credential credential, 
                                       @RequestParam String repositoryName, 
                                       @RequestParam String token) {
        User user = this.userService.findCurrentUser();
        this.userRepoService.linkUserWithRepository(credential, repositoryName, token, user);
    }
    

}
