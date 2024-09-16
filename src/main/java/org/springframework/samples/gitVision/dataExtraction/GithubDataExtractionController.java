package org.springframework.samples.gitvision.dataExtraction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.util.GeneralResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/github")
@RestController
public class GithubDataExtractionController {
    
    private GithubDataExtractionService githubDataExtractionService;

    @Value("${tokens.github}")
    private String github_token;

    @Autowired
    public GithubDataExtractionController(GithubDataExtractionService githubDataExtractionService) {
        this.githubDataExtractionService = githubDataExtractionService;
    }

    @PostMapping("/{owner}/{repo}")
    public ResponseEntity<?> extractGithubRepository(@PathVariable String owner, @PathVariable String repo, @RequestParam(required = false) String token){
        // owner = "TFG-luchersol",
        //        repo = "Git-Vision",
        try {
            String login = "luchersol";
            githubDataExtractionService.extractRepository(owner, repo, login, github_token);  
            return ResponseEntity.ok(null);  
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }

    }
}
