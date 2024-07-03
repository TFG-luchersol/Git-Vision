package org.springframework.samples.gitvision.commit.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommitsByPerson {

    private List<InnerCommitsByPerson> cont;

    public class InnerCommitsByPerson {
        private String username, avatarUrl;
        private Long numCommits;

        public InnerCommitsByPerson(String username, String avatarUrl, Long numCommits) {
            this.username = username;
            this.avatarUrl = avatarUrl;
            this.numCommits = numCommits;
        }

        public String getUsername(){
            return this.username;
        }

        public String getAvatarUrl(){
            return this.avatarUrl;
        }

        public Long getNumCommits(){
            return this.numCommits;
        }
        
    }
    
    public CommitsByPerson(List<Object[]> list){
        this.cont = new ArrayList<>();
        for (Object[] objects : list) {
            String username = String.valueOf(objects[0]) , 
                   avatarUrl = String.valueOf(objects[1]);
            Long numCommits = (long) objects[2];
            InnerCommitsByPerson innerCommitsByPerson = new InnerCommitsByPerson(username, avatarUrl, numCommits);
            this.cont.add(innerCommitsByPerson);
        }
    }

    public static CommitsByPerson of(List<Object[]> list){
        return new CommitsByPerson(list);
    }

    public Map<String, Long> parseNumCommitsByUsername(){
        return this.cont.stream().collect(Collectors.toMap(
            InnerCommitsByPerson::getUsername, InnerCommitsByPerson::getNumCommits
            )
        );
    }

}
