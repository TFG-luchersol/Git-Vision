package org.springframework.samples.gitvision.dataExtraction;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.project.ProjectRepository;
import org.springframework.samples.gitvision.project.model.Project;
import org.springframework.samples.gitvision.task.TaskRepository;
import org.springframework.samples.gitvision.task.model.Task;
import org.springframework.samples.gitvision.workspace.WorkspaceRepository;
import org.springframework.samples.gitvision.workspace.model.Workspace;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class ClockifyDataExtractionService {

    @Autowired
    RestTemplate restTemplate;

    WorkspaceRepository workspaceRepository;
    ProjectRepository projectRepository;
    TaskRepository taskRepository;

    @Autowired
    public ClockifyDataExtractionService(WorkspaceRepository workspaceRepository,
            ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.workspaceRepository = workspaceRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    private <T> T requestClockify(String url, String clockifyToken, Class<T> clazz) {
        String url2 = "https://api.clockify.me/api" + url;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-Api-Key", clockifyToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<T> response = restTemplate.exchange(url2, HttpMethod.GET, request, clazz);

        return response.getBody();
    }

    /*
     * Para la extracción de las task se debe de extraer
     * todos los proyectos creados en el workspace
     * 
     * https://api.clockify.me/api/v1/workspaces/{WORKSPACE_ID}
     * https://api.clockify.me/api/v1/workspaces/{WORKSPACE_ID}/projects
     * https://api.clockify.me/api/v1/workspaces/{WORKSPACE_ID}/projects/{
     * project_id}/tasks
     */

    @Transactional
    public void extractWorkspace(String workspaceId, String name, String clockifyToken) {
        String url = String.format("/v1/workspaces/%s", workspaceId);
        Workspace clWorkspace = requestClockify(url, clockifyToken, Workspace.class);
        clWorkspace.setName(name);
        workspaceRepository.save(clWorkspace);
        extractProject(workspaceId, clockifyToken);
    }

    @Transactional
    public void extractProject(String workspaceId, String clockifyToken) {
        String url = String.format("/v1/workspaces/%s/projects", workspaceId);
        Project[] clProjects = requestClockify(url, clockifyToken, Project[].class);
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        for (int i = 0; i < clProjects.length; i++) {
            Project clProject = clProjects[i];
            projectRepository.save(clProject);
            clProject.setWorkspace(workspace.get());
            extractTask(workspaceId, clProject.getId(), clockifyToken);
        }
        
    }

    @Transactional
    public void extractTask(String workspaceId, String projectId, String clockifyToken) {
        String url = String.format("/v1/workspaces/%s/projects/%s/tasks", workspaceId, projectId);
        Task[] clTasks = requestClockify(url, clockifyToken, Task[].class);
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        Optional<Project> project = projectRepository.findById(projectId);
        for (int i = 0; i < clTasks.length; i++) {
            Task clTask = clTasks[i];
            clTask.setProject(project.get());
            clTask.setWorkspace(workspace.get());
            taskRepository.save(clTask);
        }
    }
}
