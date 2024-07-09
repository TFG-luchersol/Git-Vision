package org.springframework.samples.gitvision.commit.model.commitsByPerson;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.samples.gitvision.util.Container;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommitsByPerson extends Container<CommitsByPersonItem> {

    public CommitsByPerson(Collection<Object[]> container) {
        super(container);
    }

    public static CommitsByPerson of(Collection<Object[]> list){
        return new CommitsByPerson(list);
    }
    
    public Map<String, Long> parseNumCommitsByUsername(){
        return this.container.stream()
                            .collect(Collectors.toMap(CommitsByPersonItem::getUsername, 
                                                    CommitsByPersonItem::getNumCommits));
    }

}

