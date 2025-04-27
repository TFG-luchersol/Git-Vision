package org.springframework.samples.gitvision.util.graphQL.models;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.samples.gitvision.util.graphQL.models.GraphQLContributionResponse.DataWrapper.Repository.PageInfo;
import org.springframework.samples.gitvision.util.graphQL.models.GraphQLContributionResponse.DataWrapper.Repository.CommitHistory.CommitEdge;
import org.springframework.samples.gitvision.util.graphQL.models.GraphQLContributionResponse.DataWrapper.Repository.CommitHistory.CommitNode;

import lombok.Data;

@Data
public class GraphQLContributionResponse {
    private DataWrapper data;

    public List<CommitEdge> getEdges() {
        return this.getData()
                .getRepository()
                .getDefaultBranchRef()
                .getTarget()
                .getHistory()
                .getEdges();
    }

    public PageInfo getPageInfo() {
        return this.getData()
                .getRepository()
                .getDefaultBranchRef()
                .getTarget()
                .getHistory()
                .getPageInfo();
    }

    public List<CommitNode> getNodes() {
        return this.getData()
                .getRepository()
                .getDefaultBranchRef()
                .getTarget()
                .getHistory()
                .getEdges()
                .stream()
                .map(edge -> edge.getNode())
                .toList();
    }

    @Data
    public static class DataWrapper {
        private Repository repository;
        private RateLimit rateLimit;

        @Data
        public static class Repository {
            private DefaultBranchRef defaultBranchRef;

            @Data
            public static class DefaultBranchRef {
                private String name;
                private Target target;

                @Data
                public static class Target {
                    private CommitHistory history;
                }
            }

            @Data
            public static class CommitHistory {
                private List<CommitEdge> edges;
                private PageInfo pageInfo;

                @Data
                public static class CommitEdge {
                    private CommitNode node;
                }

                @Data
                public static class CommitNode {
                    private String committedDate;
                    private int additions;
                    private int deletions;
                    private Author author;
                }

                @Data
                public static class Author {
                    private String name;
                }
            }

            @Data
            public static class PageInfo {
                private String endCursor;
                private boolean hasNextPage;
            }
        }
        @Data
        public static class RateLimit {
            private int limit;
            private int cost;
            private int remaining;
            private String resetAt;
        }

    }


}
