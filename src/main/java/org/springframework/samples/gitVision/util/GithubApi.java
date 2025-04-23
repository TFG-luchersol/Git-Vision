package org.springframework.samples.gitvision.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.exceptions.ResourceNotFoundException;
import org.springframework.samples.gitvision.issue.model.Issue;
import org.springframework.samples.gitvision.relations.repository.model.GVRepo;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

public class GithubApi {

    private RestTemplate restTemplate = new RestTemplate();
    private String githubToken;
    private String repositoryName;

    private GithubApi(String repositoryName, String githubToken) {
        this.githubToken = githubToken;
        this.repositoryName = repositoryName;
    }

    public static GithubApi connect(String repositoryName, String githubToken) {
        return new GithubApi(repositoryName, githubToken);
    }

    public static GithubApi connect(GVRepo gvRepo) {
        return new GithubApi(gvRepo.getName(), gvRepo.getToken());
    }

    private <T> T requestGithub(String url, Class<T> clazz) {
        String url_template =  "https://api.github.com" + url;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization","Bearer " + githubToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<T> response = restTemplate.exchange(url_template, HttpMethod.GET, request, clazz);

        return response.getBody();
    }

    public List<Commit> getCommitsByPage(Integer page, Integer perPage){
        String url = String.format("/repos/%s/commits?page=%d&per_page=%d", repositoryName, page, perPage);
        JsonNode[] commits = requestGithub(url, JsonNode[].class);
        return Arrays.stream(commits).map(Commit::parseJson).toList();
    }

    public List<Commit> getCommitsByPageAndAuthor(Integer page, Integer perPage){
        String url = String.format("/repos/%s/commits?page=%d&per_page=%d", repositoryName, page, perPage);
        JsonNode[] commits = requestGithub(url, JsonNode[].class);
        return Arrays.stream(commits).map(Commit::parseJson).toList();
    }

    public List<Issue> getIssuesByPage(Integer page, Integer perPage){
        String url = String.format("/repos/%s/issues?state=all&page=%d&per_page=%d", repositoryName, page, perPage);
        JsonNode[] issues = requestGithub(url, JsonNode[].class);
        return Arrays.stream(issues).map(Issue::parseJson).toList();
    }

    public Issue getIssueByExactTitle(String title) {
        try {
            String encodedTitle = URLEncoder.encode("\"" + title + "\"", StandardCharsets.UTF_8);
            String url = String.format("/search/issues?q=repo:%s+type:issue+in:title+%s", repositoryName, encodedTitle);

            JsonNode response = requestGithub(url, JsonNode.class);
            JsonNode items = response.get("items");

            if (items != null && items.isArray()) {
                for (JsonNode item : items) {
                    String issueTitle = item.get("title").asText();
                    if (issueTitle.equals(title)) {
                        return Issue.parseJson(item);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw ResourceNotFoundException.of("No se ha encontrado issue con titulo " + title);
    }

}
