package org.springframework.samples.gitvision.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.project.model.Project;
import org.springframework.samples.gitvision.task.model.Task;
import org.springframework.samples.gitvision.workspace.model.TimeEntry;
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

    public static List<Task> getTasksByName(String workspaceId, String name, String clockifyToken) {
        List<Project> projects = getProjects(workspaceId, clockifyToken);
        List<Task> tasks = new ArrayList<>();
        for (Project project : projects) {
            String url = String.format("/v1/workspaces/%s/projects/%s/tasks?name=%s&strict-name-search=true",
                                       workspaceId, project.getId(), name);
            Task[] tasksSlot = requestClockify(url, clockifyToken, Task[].class);
            Collections.addAll(tasks, tasksSlot);
        }
        return tasks;
    }

    public static Map<String, Long> getTimeByUser(String workspaceId, String clockifyToken) {
        String url = String.format("/v1/workspaces/%s/users", workspaceId);
        List<UserProfile> users = ClockifyApi.getUsers(workspaceId, clockifyToken);
        Map<String, Long> res = new HashMap<>();
        for (UserProfile user : users) {
            url = String.format("/v1/workspaces/%s/user/%s/time-entries", workspaceId, user.getId());
            TimeEntry[] timeEntries = ClockifyApi.requestClockify(url, clockifyToken, TimeEntry[].class);
            String name = user.getName();
            Long time = Arrays.stream(timeEntries).mapToLong(entry -> entry.getTimeInterval().getDuration().getNano())
                    .sum();
            res.put(name, time);
        }

        return res;
    }

    /**
     * Obtiene un mapa que asocia a cada usuario con el tiempo total (en nanosegundos)
     * que ha aportado a todas las tareas con un nombre específico dentro de un workspace.
     *
     * @param workspaceId    ID del workspace de Clockify.
     * @param taskName       Nombre de la tarea a filtrar.
     * @param clockifyToken  Token de autenticación para la API de Clockify.
     * @return Un mapa donde la clave es el nombre o ID del usuario y el valor es
     *         el tiempo total registrado (en nanosegundos) en tareas con el nombre especificado.
     */
    public static Map<String, Long> getTimeByUserByTaskName(String workspaceId, String taskName, String clockifyToken) {
        List<UserProfile> users = ClockifyApi.getUsers(workspaceId, clockifyToken);
        Map<String, Long> res = new HashMap<>();
        Set<String> tasks = getTasksByName(workspaceId, taskName, clockifyToken)
                                .stream()
                                .map(Task::getId)
                                .collect(Collectors.toSet());
        for (UserProfile user : users) {
            Long time = 0L;
            String name = user.getName();
            for (String taskId : tasks) {
                String url = String.format("/v1/workspaces/%s/user/%s/time-entries?task=%s",
                                           workspaceId, user.getId(), taskId);
                TimeEntry[] timeEntries = ClockifyApi.requestClockify(url, clockifyToken, TimeEntry[].class);
                time += Arrays.stream(timeEntries)
                        .filter(entry -> tasks.contains(entry.getTaskId()))
                        .mapToLong(entry -> entry.getTimeInterval().getDuration().getNano())
                        .sum();

            }
            res.put(name, time);
        }

        return res;
    }


}
