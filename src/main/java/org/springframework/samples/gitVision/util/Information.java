package org.springframework.samples.gitvision.util;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class Information {

    private Map<String, Object> information;

    public Information(){
        this.information = new HashMap<String, Object>();
    }
    
    public static Information empty(){
        return new Information();
    }

    public Information put(String key, Object value){
        this.information.put(key, value);
        return this;
    }

    public static Information create(String key, Object value){
        return empty().put(key, value);
    }
}
