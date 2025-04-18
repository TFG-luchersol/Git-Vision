package org.springframework.samples.gitvision.util;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class Information extends HashMap<String, Object> {

    public Information(){
        super(new HashMap<String, Object>());
    }

    public Information(Map<String, Object> information){
        super(information);
    }

    public static Information empty(){
        return new Information();
    }

    public static Information of(Map<String, Object> information){
        return new Information(information);
    }

    public static Information of(String message){
        return new Information(new HashMap<>(Map.of("message", message)));
    }

    public Information put(String key, Object value){
        super.put(key, value);
        return this;
    }

    public static Information create(String key, Object value){
        return empty().put(key, value);
    }

    public <T> T getAs(String key, Class<T> clazz) {
        return clazz.cast(this.get(key));
    }
}
