package org.springframework.samples.gitvision.commit.model;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.samples.gitvision.model.BaseEntity;
import org.springframework.samples.gitvision.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "commits")
public class Commit extends BaseEntity {
    
    String message;

    @NotNull
    LocalDateTime date;

    @ManyToOne
    User author;

    @PositiveOrZero
    int additions;

    @PositiveOrZero
    int deletions;
    
    @Transient
    public CommitType getCommitType() {
        String regex = "^\\s*\\[\\s*(\\w+)\\s*\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(getMessage());
        if(matcher.find()) {
            String type = matcher.group(1).trim().toUpperCase();
            try {
                return CommitType.valueOf(type);
            } catch (Exception e) {
                return null;
            }
        } 
        return null;
    }

}
