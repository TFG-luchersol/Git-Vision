package org.springframework.samples.gitvision.file;

import java.util.HashMap;
import java.util.Map;

public class PercentageLanguages {
    
    Map<String, Double> percentages;
    int cont;
    boolean isDraftMode;
    
    public PercentageLanguages(){
        this.percentages = new HashMap<>();
        this.isDraftMode = true;
        this.cont = 0;
    }

    public static PercentageLanguages empty(){
        return new PercentageLanguages();
    }

    public double get(String language) {
        return this.percentages.getOrDefault(language, 0.);
    }

    public double getUnknown(){
        return this.percentages.getOrDefault("Unknown", 0.);
    }

    public boolean contains(String language) {
        return this.percentages.containsKey(language);
    }

    public void add(String language) {
        double newValue = get(language) + 1;
        this.percentages.put(language, newValue);
        this.cont++;
    }

    public void calc(){
        this.percentages.replaceAll((key, value) -> value/this.cont);
        this.isDraftMode = false;
    }
}
