package org.springframework.samples.gitvision.util.graphQL.models;

import lombok.Data;

import java.util.List;

@Data
public class GraphQLCommitResponse {
    private DataWrapper data;

    @Data
    public static class DataWrapper {
        private Repository repository;

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
                    private String email;
                }
            }

            @Data
            public static class PageInfo {
                private String endCursor;
                private boolean hasNextPage;
            }
        }
    }
}
