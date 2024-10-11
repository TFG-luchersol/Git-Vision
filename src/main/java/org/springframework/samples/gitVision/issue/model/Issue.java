package org.springframework.samples.gitvision.issue.model;

import java.util.List;

import org.kohsuke.github.GHIssue;
import org.springframework.samples.gitvision.commit.model.Commit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Issue {
    
    private String title;
    private Integer number;
    private String body;
    private String state;
    private List<Commit> commits;

    public static Issue parse(GHIssue ghIssue){
        Issue issue = new Issue();
        issue.setTitle(ghIssue.getTitle());
        issue.setNumber(ghIssue.getNumber());
        issue.setBody(ghIssue.getBody());
        issue.setState(ghIssue.getState().toString());
        return issue;
    }

}
