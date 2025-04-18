package org.springframework.samples.gitvision.util.graphQL.models;

import java.util.List;

import lombok.Data;

@Data
public class GraphQLChangesInFilesByUser {

    private Repository repository;

    public List<Commit> getNodes() {
        return this.getRepository().getDefaultBranchRef().getTarget().getHistory().getEdges().stream().map(Edge::getNode).toList();
    }

    public PageInfo getPageInfo() {
        return this.getRepository().getDefaultBranchRef().getTarget().getHistory().getPageInfo();
    }

    @Data
    public static class Repository {
        private DefaultBranchRef defaultBranchRef;
    }

    @Data
    public static class DefaultBranchRef {
        private Target target;
    }

    @Data
    public static class Target {
        private CommitHistory history;
    }

    @Data
    public static class CommitHistory {
        private List<Edge> edges;
        private PageInfo pageInfo;
    }

    @Data
    public static class Edge {
        private Commit node;
    }

    @Data
    public static class Commit {
        private String committedDate;
        private FileHistory files;
    }

    @Data
    public static class FileHistory {
        private List<FileEdge> edges;
    }

    @Data
    public static class FileEdge {
        private File node;
    }

    @Data
    public static class File {
        private String path;
        private int additions;
        private int deletions;
    }

    @Data
    public static class PageInfo {
        private String endCursor;
        private boolean hasNextPage;
    }
}

