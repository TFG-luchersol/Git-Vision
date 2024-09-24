package org.springframework.samples.gitvision.commit.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.model.entity.EntityIdString;
import org.springframework.samples.gitvision.repository.model.Repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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

    @NotNull
    LocalDateTime date;

    @PositiveOrZero
    int additions;

    @PositiveOrZero
    int deletions;

    @Enumerated(EnumType.STRING)
    CommitType commitType;
    
    @ManyToOne
    GithubUser author;

    @ManyToOne
    Repository repository;

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
