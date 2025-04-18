package org.springframework.samples.gitvision.task;

import java.util.List;

import org.springframework.samples.gitvision.task.model.Task;
import org.springframework.samples.gitvision.user.GVUserRepository;
import org.springframework.samples.gitvision.user.model.GVUser;
import org.springframework.samples.gitvision.util.ClockifyApi;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final GVUserRepository gvUserRepository;

    public TaskService(GVUserRepository gvUserRepository){
        this.gvUserRepository = gvUserRepository;
    }

    public Long timeByName(String workspaceId, String projectId, String name, Long userId){
        GVUser user = gvUserRepository.findById(userId).orElseThrow();
        String clockifyToken = user.getClockifyToken();
        List<Task> tasks = ClockifyApi.getTasks(workspaceId, projectId, clockifyToken);
        Long totalDuration = tasks.stream()
                                    .filter(t -> t.getName().equals(name))
                                    .mapToLong(task -> task.getDuration().toMillis()).sum();
        return totalDuration;
    }

}
