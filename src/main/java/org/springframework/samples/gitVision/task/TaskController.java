package org.springframework.samples.gitvision.task;

import org.springframework.samples.gitvision.auth.payload.response.BadResponse;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.auth.payload.response.OkResponse;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.util.Information;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    
    TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @GetMapping("/{workspaceId}/{projectId}/time")
    public MessageResponse time(@PathVariable String workspaceId,
                                @PathVariable String projectId,
                                @RequestParam(required = false) Long id, 
                                @RequestParam(required = false) String name,
                                @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        Long userId = userDetailsImpl.getId();
        if(Boolean.logicalXor(id == null, name == null))
            return BadResponse.of("Debe proporcionar solo un ID o un nombre, no ambos.");

        Long duration = id != null ?
                this.taskService.timeById(workspaceId, projectId, id, userId) :
                this.taskService.timeByName(workspaceId, projectId, name, userId);
        Information information = Information.create("duration", duration);
        return OkResponse.of(information);
    }
    
}
