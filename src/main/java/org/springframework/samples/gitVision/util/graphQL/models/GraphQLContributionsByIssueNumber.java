package org.springframework.samples.gitvision.util.graphQL.models;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class GraphQLContributionsByIssueNumber {

    private DataWrapper data;
    private List<ErrorResponse> errors;

    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

    public String getErrorMessage() {
        return this.errors.stream().map(ErrorResponse::getMessage).collect(Collectors.joining("\n"));
    }

    public TimelineItems getTimelineItems() {
        return this.data.repository.issue.timelineItems;
    }

    public List<TimelineNode> getNodes() {
        return this.getTimelineItems().nodes;
    }

    public PageInfo getPageInfo() {
        return this.getTimelineItems().pageInfo;
    }

    public boolean hasNextPage() {
        return getPageInfo().hasNextPage;
    }

    public String getEndCursor() {
        return getPageInfo().endCursor;
    }

    @Data
    public static class DataWrapper {
        private Repository repository;
    }

    @Data
    public static class Repository {
        private Issue issue;
    }

    @Data
    public static class Issue {
        private TimelineItems timelineItems;
    }

    @Data
    public static class TimelineItems {
        private PageInfo pageInfo;
        private List<TimelineNode> nodes;
    }

    @Data
    public static class PageInfo {
        private String endCursor;
        private boolean hasNextPage;
    }

    @Data
    public static class TimelineNode {
        private Commit commit;

        public int getAdditions(){
            return commit.additions;
        }

        public int getDeletions(){
            return commit.deletions;
        }

        public String getAuthorName(){
            return commit.author.name;
        }
    }

    @Data
    public static class Commit {
        private int additions;
        private int deletions;
        private Author author;
    }

    @Data
    public static class Author {
        private String name;
    }

    @Data
    public static class ErrorResponse {
        private String type;
        private List<String> path;
        private List<Location> locations;
        private String message;
    }

    @Data
    public static class Location {
        private int line;
        private int column;
    }

}
