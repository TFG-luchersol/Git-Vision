package org.springframework.samples.gitvision.commit.model;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GHCommit.File;
import org.springframework.samples.gitvision.change.model.Change;
import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.issue.model.Issue;
import org.springframework.samples.gitvision.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Commit {

    String sha;
    
    String message;

    String body;

    LocalDateTime date;

    int additions;

    int deletions;

    CommitType commitType;

    GithubUser author;

    List<File> files;

    List<Issue> issues;

    public Change getChange(){
        return Change.of(additions, deletions);
    }

    public void setMessage(String message) {
        this.message = message;
        this.calcCommitType();
        this.calcCommitBody();
    }

    private void calcCommitBody() {
        String[] split = this.message.split("\n\n");
        if(split.length > 1)
            this.body = split[1];
    }

    private void calcCommitType() {
        if(this.message != null){
            this.commitType = null;
        } else {
            String regex = "^\\s*\\[\\s*(\\w+)\\s*\\]";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(getMessage());
            if(matcher.find()) {
                String type = matcher.group(1).trim().toUpperCase();
                try {
                    this.commitType = CommitType.valueOf(type);
                } catch (Exception e) {
                    this.commitType = null;
                }
            } else {
                this.commitType = null;
            }
        }
    }

    public static Commit parse(GHCommit ghCommit){
        GithubUser githubUser = new GithubUser();
        Commit commit = new Commit();
        try {
            githubUser = GithubUser.parseGitUser(ghCommit.getCommitShortInfo().getAuthor());
            commit.setFiles(ghCommit.listFiles().toList());
            commit.setIssues(new ArrayList<>());
            commit.setSha(ghCommit.getSHA1());
            commit.setMessage(ghCommit.getCommitShortInfo().getMessage());
            commit.setAdditions(ghCommit.getLinesAdded());
            commit.setDeletions(ghCommit.getLinesDeleted());
            commit.setDate(EntityUtils.parseDateToLocalDateTimeUTC(ghCommit.getCommitDate()));
            commit.setAuthor(githubUser);
        } catch (IOException e) {
            return null;
        }
        return commit;
    }

    public static Commit parseJson(JsonNode jsonNode){
        Commit commit = new Commit();
        commit.setSha(jsonNode.get("sha").textValue());
        commit.setMessage(jsonNode.get("commit").get("message").textValue());
        return commit;
    }

    public Set<Integer> getIssueNumbers() {
        Set<Integer> set = new HashSet<>();
        Pattern pattern = Pattern.compile("\\s*#(\\d+)\\s*");
        Matcher matcher = pattern.matcher(getMessage());
        while (matcher.find()) {
            Integer number = Integer.valueOf(matcher.group(1));
            set.add(number);
        }
        return set;
    }

}
