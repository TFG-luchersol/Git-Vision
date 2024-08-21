package org.springframework.samples.gitvision.dataExtraction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public void extractGithubRepository(@PathVariable String owner, @PathVariable String repo){
        // owner = "TFG-luchersol",
        //        repo = "Git-Vision",
        String login = "luchersol",
               token = this.github_token;
        githubDataExtractionService.extractRepository(owner, repo, login, token);    }
}
