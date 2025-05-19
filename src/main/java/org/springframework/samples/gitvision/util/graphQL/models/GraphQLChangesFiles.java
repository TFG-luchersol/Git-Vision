package org.springframework.samples.gitvision.util.graphQL.models;

import lombok.Data;
import java.util.List;

@Data
public class GraphQLChangesFiles {

    private GitHubData data;

    public List<CommitNode> getNodes() {
        return this.getData()
                .getRepository()
                .getDefaultBranchRef()
                .getTarget()
                .getHistory()
                .getEdges()
                .stream()
                .map(CommitEdge::getNode).toList();
    }

    public PageInfo getPageInfo() {
        return this.getData().getRepository().getDefaultBranchRef().getTarget().getHistory().getPageInfo();
    }

    @Data
    public static class GitHubData {
        private Repository repository;
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
        private List<CommitEdge> edges;
        private PageInfo pageInfo;
    }

    @Data
    public static class CommitEdge {
        private CommitNode node;
    }

    @Data
    public static class CommitNode {
        private String committedDate;
        private int additions;
        private int deletions;
    }



    @Data
    public static class PageInfo {
        private String endCursor;
        private boolean hasNextPage;
    }
}
