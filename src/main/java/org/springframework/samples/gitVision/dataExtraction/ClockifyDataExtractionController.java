package org.springframework.samples.gitvision.dataExtraction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/clockify")
@RestController
public class ClockifyDataExtractionController {
        
    private ClockifyDataExtractionService clockifyDataExtractionService;

    @Value("${tokens.clockify}")
    private String clockify_token;

    @Autowired
    public ClockifyDataExtractionController(ClockifyDataExtractionService clockifyDataExtractionService) {
        this.clockifyDataExtractionService = clockifyDataExtractionService;
    }

    @PostMapping("/workspaces/{workspaceId}")
    public void extractClockifyWorkspace(@PathVariable String workspaceId, @RequestParam String name){
        try {
            String token = this.clockify_token;
            clockifyDataExtractionService.extractWorkspace(workspaceId, name, token); 
        } catch (Exception e) {
            // TODO: handle exception
        }
         
    }
}
