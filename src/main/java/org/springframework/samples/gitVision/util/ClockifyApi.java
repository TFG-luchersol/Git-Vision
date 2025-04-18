package org.springframework.samples.gitvision.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.project.model.Project;
import org.springframework.samples.gitvision.task.model.Task;
import org.springframework.samples.gitvision.workspace.model.UserProfile;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

public class ClockifyApi {
    
    static RestTemplate restTemplate = new RestTemplate();

    public static <T> T requestClockify(String url, String clockifyToken, Class<T> clazz) {
        String url_template = "https://api.clockify.me/api" + url;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-Api-Key", clockifyToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<T> response = restTemplate.exchange(url_template, HttpMethod.GET, request, clazz);

        return response.getBody();
    }

    public static JsonNode getCurrentUser(String clockifyToken){
        String url = "/v1/user";
        JsonNode json = requestClockify(url, clockifyToken, JsonNode.class);
        return json;
    }

    public static JsonNode getWorkspace(String workspaceId, String clockifyToken) {
        String url = String.format("/v1/workspaces/%s", workspaceId);
        JsonNode workspace = requestClockify(url, clockifyToken, JsonNode.class);
        return workspace;
    }

    public static List<UserProfile> getUsers(String workspaceId, String clockifyToken) {
        String url = String.format("/v1/workspaces/%s/users", workspaceId);
        UserProfile[] users = requestClockify(url, clockifyToken, UserProfile[].class);
        return Arrays.asList(users);
    }
    public static List<Project> getProjects(String workspaceId, String clockifyToken) {
        String url = String.format("/v1/workspaces/%s/projects", workspaceId);
        Project[] projects = requestClockify(url, clockifyToken, Project[].class);
        return Arrays.asList(projects);
    }

    public static List<Task> getTasks(String workspaceId, String projectId, String clockifyToken) {
        String url = String.format("/v1/workspaces/%s/projects/%s/tasks", workspaceId, projectId);
        Task[] tasks = requestClockify(url, clockifyToken, Task[].class);
        return Arrays.asList(tasks);
    }
    
}
