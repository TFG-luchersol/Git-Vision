package org.springframework.samples.gitvision.commit.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommitsByPerson {
    
    private String username, avatarUrl;
    private Long numCommits;

    public CommitsByPerson(String username, String avatarUrl, Long numCommits) {
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.numCommits = numCommits;
    }

    public static Map<String, Long> parseNumCommitsByUsername(List<CommitsByPerson> ls){
        return ls.stream().collect(Collectors.toMap(
            CommitsByPerson::getUsername, CommitsByPerson::getNumCommits
            )
        );
    }

}
