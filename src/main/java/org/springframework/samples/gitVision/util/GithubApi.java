package org.springframework.samples.gitvision.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.issue.model.Issue;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

public class GithubApi {
    
    static RestTemplate restTemplate = new RestTemplate();

    private static <T> T requestGithub(String url, String githubToken, Class<T> clazz) {
        String url_template =  "https://api.github.com" + url;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization","Bearer " + githubToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<T> response = restTemplate.exchange(url_template, HttpMethod.GET, request, clazz);

        return response.getBody();
    }

    public static List<Commit> getCommitsByPage(String repositoryName, Integer page, Integer perPage, String githubToken){
        String url = String.format("/repos/%s/commits?page=%d&per_page=%d", repositoryName, page, perPage);
        JsonNode[] commits = requestGithub(url, githubToken, JsonNode[].class);
        return Arrays.stream(commits).map(Commit::parseJson).toList();
    }

    public static List<Issue> getIssuesByPage(String repositoryName, Integer page, Integer perPage, String githubToken){
        String url = String.format("/repos/%s/issues?state=all&page=%d&per_page=%d", repositoryName, page, perPage);
        JsonNode[] issues = requestGithub(url, githubToken, JsonNode[].class);
        return Arrays.stream(issues).map(Issue::parseJson).toList();
    }
    
}
