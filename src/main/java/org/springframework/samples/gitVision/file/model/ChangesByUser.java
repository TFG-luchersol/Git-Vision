package org.springframework.samples.gitvision.file.model;

import org.springframework.samples.gitvision.change.model.Change;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignora campos desconocidos al deserializar
@JsonInclude(JsonInclude.Include.NON_NULL) 
@Getter
@Setter
public class ChangesByUser {
    
    private Map<String, List<Change>> changesByUser;

    public ChangesByUser(){
        this.changesByUser = new HashMap<>();
    }

    public void add(String user, Change change){
        if(changesByUser.containsKey(user)){
            changesByUser.get(user).add(change);
        } else {
            changesByUser.put(user, new ArrayList<>(List.of(change)));
        }
    }

    public Map<String, Change> suma(){
        return changesByUser.keySet().stream()
        .collect(Collectors.toMap(Function.identity(), 
                                  u -> changesByUser.get(u).stream().reduce(Change::staticMerge).get()));
    }

    @Override
    public String toString() {
        return this.changesByUser.toString();
    }

}
