package org.springframework.samples.gitVision.commit.stats;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.jsonwebtoken.lang.Collections;
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

    // public static CommitsByPerson parse(Object[] array){
    //     String username = String.valueOf(array[0]),
    //             avatarUrl = String.valueOf(array[1]);
    //     Long numCommits = (long) array[2];
    //     return new CommitsByPerson(username, avatarUrl, numCommits);
    // }
    
    // public static List<CommitsByPerson> parse(List<Object[]> array){
    //     return array.stream().map(CommitsByPerson::parse).collect(Collectors.toList());
    // }

    // public static Map<String, Long> parseNumCommitsByUsername(List<Object[]> array){
    //     return array.stream().collect(Collectors.toMap(
    //         obj -> String.valueOf(obj[0]), 
    //         obj -> (long) obj[2]
    //         )
    //         );
    // }
}
