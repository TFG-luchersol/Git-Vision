package org.springframework.samples.gitvision.file.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PercentageLanguages {
    
    @JsonProperty
    private Map<String, Double> percentages;
    
    public PercentageLanguages(){
        this.percentages = new HashMap<>();
    }

    public PercentageLanguages(Map<String, Double> percentages){
        this.percentages = percentages;
    }

    public static PercentageLanguages of(Map<String, Long> files){
        long cont = files.values().stream().mapToLong(Long::valueOf).sum();
        Map<String, Double> percentajes = files.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> 1.* entry.getValue() / cont));
        return new PercentageLanguages(percentajes);
    }
}
