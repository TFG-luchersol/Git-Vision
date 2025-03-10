package org.springframework.samples.gitvision.task;

import java.util.List;

import org.springframework.samples.gitvision.task.model.Task;
import org.springframework.samples.gitvision.util.ClockifyApi;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    
    public Long timeByName(String workspaceId, String projectId, String name, Long userId){
        List<Task> tasks = ClockifyApi.getTasks(null, null, null);
        Long totalDuration = tasks.stream()
                                    .filter(t -> t.getName().equals(name))
                                    .mapToLong(task -> task.getDuration().toMillis()).sum();
        return totalDuration;
    }

    public Long timeById(String workspaceId, String projectId, Long issueId, Long userId){
        List<Task> tasks = ClockifyApi.getTasks(null, null, null);
        Long totalDuration = tasks.stream()
                                    .filter(t -> t.getName().equals("#"+issueId))
                                    .mapToLong(task -> task.getDuration().toMillis()).sum();
        return totalDuration;
    }
}
