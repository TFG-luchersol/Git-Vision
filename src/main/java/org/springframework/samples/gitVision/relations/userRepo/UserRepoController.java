package org.springframework.samples.gitvision.relations.userRepo;

import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.util.Information;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/relation/user_repository")
@Tag(name = "Relation User_Repository")
public class UserRepoController {
    
    UserRepoService userRepoService;

    @Autowired
    public UserRepoController(UserRepoService userRepoService) {
        this.userRepoService = userRepoService;
    }

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

}
