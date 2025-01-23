package org.springframework.samples.gitvision.issue.model;

import java.util.List;

import org.kohsuke.github.GHIssue;
import org.springframework.samples.gitvision.commit.model.Commit;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Issue {
    
    private String title;
    private Integer number;
    private String body;
    private String state;

    public static Issue parse(GHIssue ghIssue){
        Issue issue = new Issue();
        issue.setTitle(ghIssue.getTitle());
        issue.setNumber(ghIssue.getNumber());
        issue.setBody(ghIssue.getBody());
        issue.setState(ghIssue.getState().toString());
        return issue;
    }

    public static Issue parseJson(JsonNode jsonNode){
        Issue issue = new Issue();
        issue.setTitle(jsonNode.get("title").textValue());
        issue.setNumber(jsonNode.get("number").intValue());
        issue.setState(jsonNode.get("state").textValue());
        return issue;
    }

}
