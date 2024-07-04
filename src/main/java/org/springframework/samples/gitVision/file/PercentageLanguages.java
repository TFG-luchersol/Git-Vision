package org.springframework.samples.gitvision.file;

import java.util.HashMap;
import java.util.Map;

public class PercentageLanguages {
    
    Map<String, Integer> percentages;
    
    public PercentageLanguages(){
        this.percentages = new HashMap<>();
    }

    public static PercentageLanguages empty(){
        return new PercentageLanguages();
    }

    public int get(String language) {
        return this.percentages.getOrDefault(language, 0);
    }

    public int getUnknown(){
        return this.percentages.getOrDefault("Unknown", 0);
    }

    public boolean contains(String language) {
        return this.percentages.containsKey(language);
    }

    public void add(String language) {
        Integer newValue = get(language) + 1;
        this.percentages.put(language, newValue);
    }
}
