package org.springframework.samples.gitvision.task;

import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.util.Checker;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@RestController
@RequestMapping("/api/v1/tasks")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @GetMapping("/{workspaceId}/{projectId}/time")
    public ResponseEntity<Long> time(@PathVariable String workspaceId,
                                @PathVariable String projectId,
                                @RequestParam(required = false) Long id,
                                @RequestParam(required = false) String name,
                                @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        Long userId = userDetailsImpl.getId();

        Checker.checkOrBadRequest((id == null) ^ (name == null),
                          "Debe proporcionar solo un ID o un nombre, no ambos.");
        String taskName = name != null ? name : "#"+id;
        Long duration = this.taskService.timeByName(workspaceId, projectId, taskName, userId);
        return ResponseEntity.ok(duration);
    }

}
