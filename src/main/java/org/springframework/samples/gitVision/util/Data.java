package org.springframework.samples.gitvision.util;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class Data {

    private Map<String, Object> data;

    public Data(){
        this.data = new HashMap<String, Object>();
    }
    
    public static Data empty(){
        return new Data();
    }

    public Data put(String key, Object value){
        this.data.put(key, value);
        return this;
    }
}
