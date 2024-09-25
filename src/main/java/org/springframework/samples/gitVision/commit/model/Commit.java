package org.springframework.samples.gitvision.commit.model;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kohsuke.github.GHCommit;
import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.model.entity.EntityIdString;
import org.springframework.samples.gitvision.util.EntityUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "commits")
@AllArgsConstructor
@NoArgsConstructor
public class Commit extends EntityIdString {
    
    String message;

    LocalDateTime date;

    int additions;

    int deletions;

    CommitType commitType;

    GithubUser author;

    public void setMessage(String message) {
        this.message = message;
        this.calcCommitType();
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
        Commit commit = null;
        try {
            commit = new Commit(ghCommit.getCommitShortInfo().getMessage(), EntityUtils.parseDateToLocalDateTimeUTC(ghCommit.getCommitDate()) , ghCommit.getLinesAdded(), ghCommit.getLinesDeleted(), null, githubUser);
            commit.calcCommitType();
        } catch (IOException e) {
            return null;
        }
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Commit other = (Commit) obj;
        return this.id != null && Objects.equals(other.id, this.id);
    }
    

}
