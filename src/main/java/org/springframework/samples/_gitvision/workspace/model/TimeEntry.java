package org.springframework.samples.gitvision.workspace.model;

import java.time.Duration;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeEntry {

    private String taskId;

    private TimeInterval timeInterval;

    @Data
    public static class TimeInterval {
        private Duration duration;
        private LocalDateTime end;
        private LocalDateTime start;
    }
}
