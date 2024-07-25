package org.springframework.samples.gitvision.file.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.samples.gitvision.util.LanguageDetector;

public class PercentageLanguages {
    
    Map<String, Double> percentages;
    
    public PercentageLanguages(){
        this.percentages = new HashMap<>();
    }

    public PercentageLanguages(Map<String, Double> percentages){
        this.percentages = percentages;
    }

    public static PercentageLanguages empty(){
        return new PercentageLanguages();
    }

    public Double get(String language) {
        return this.percentages.getOrDefault(language, 0.);
    }

    public Double getUnknown(){
        return this.percentages.getOrDefault("Unknown", 0.);
    }

    public boolean contains(String language) {
        return this.percentages.containsKey(language);
    }

    public static PercentageLanguages of(Map<String, Double> percentages){
        return new PercentageLanguages(percentages);
    }

    public static PercentageLanguages of(List<File> files){
        Map<String, Double> percentajes = new HashMap<>();
        int cont = files.size();
        for (File file : files) {
            String languaje = LanguageDetector.detectLanguageByExtension(file);
            Double newValue = percentajes.getOrDefault(languaje, 0.) + 1./cont;
            percentajes.put(languaje, newValue);
        }
        return PercentageLanguages.of(percentajes);
    }
}
