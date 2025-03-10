package org.springframework.samples.gitvision.task;

import java.util.List;

import org.springframework.samples.gitvision.task.model.Task;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.user.UserRepository;
import org.springframework.samples.gitvision.util.ClockifyApi;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final UserRepository userRepository;

    public TaskService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    
    public Long timeByName(String workspaceId, String projectId, String name, Long userId){
        User user = userRepository.findById(userId).orElseThrow();
        String clockifyToken = user.getClockifyToken();
        List<Task> tasks = ClockifyApi.getTasks(workspaceId, projectId, clockifyToken);
        Long totalDuration = tasks.stream()
                                    .filter(t -> t.getName().equals(name))
                                    .mapToLong(task -> task.getDuration().toMillis()).sum();
        return totalDuration;
    }

}
