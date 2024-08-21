package org.springframework.samples.gitvision.dataExtraction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.gitvision.dataExtraction.GithubDataExtractionService;

@SpringBootTest
public class GithubDataExtractionServiceTest {

    @Autowired
    GithubDataExtractionService githubDataExtractionService;

    @Value("${tokens.github}")
    private String github_token;

    @Test
    void testExtractRepository() {
        String owner = "TFG-luchersol",
               repo = "Git-Vision",
               login = "luchersol",
               token = this.github_token;
        githubDataExtractionService.extractRepository(owner, repo, login, token);
    }
    

}
