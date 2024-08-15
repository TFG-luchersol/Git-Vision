package org.springframework.samples.gitvision.commit.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.model.entity.EntityIdString;
import org.springframework.samples.gitvision.repository.Repository;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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

    @PrePersist
    @PreUpdate
    public void calcCommitType() {
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

    public List<Integer> getIssueNumbers() {
        List<Integer> ls = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\s*#(\\d+)\\s*");
        Matcher matcher = pattern.matcher(getMessage());
        while (matcher.find()) {
            Integer number = Integer.valueOf(matcher.group(1));
            ls.add(number);
        }
        return ls;
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
