package org.springframework.samples.gitvision.dataExtraction;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/linker")
@RestController
public class GitHubClockifyLinkerController {
    
    GitHubClockifyLinkerService gitHubClockifyLinkerService;
}
