package org.springframework.samples.gitvision.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.project.model.Project;
import org.springframework.samples.gitvision.task.model.Task;
import org.springframework.samples.gitvision.workspace.model.Workspace;
import org.springframework.web.client.RestTemplate;

public class ClockifyApi {
    
    static RestTemplate restTemplate = new RestTemplate();

    private static <T> T requestClockify(String url, String clockifyToken, Class<T> clazz) {
        String url_template = "https://api.clockify.me/api" + url;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-Api-Key", clockifyToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<T> response = restTemplate.exchange(url_template, HttpMethod.GET, request, clazz);

        return response.getBody();
    }

    public static Map<String, String> getCurrentUser(String clockifyToken){
        String url = "/v1/user";
        Map<String, String> map = requestClockify(url, clockifyToken, HashMap.class);
        return map;
    }

    public static Workspace getWorkspace(String workspaceId, String clockifyToken) {
        String url = String.format("/v1/workspaces/%s", workspaceId);
        Workspace workspace = requestClockify(url, clockifyToken, Workspace.class);
        return workspace;
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
