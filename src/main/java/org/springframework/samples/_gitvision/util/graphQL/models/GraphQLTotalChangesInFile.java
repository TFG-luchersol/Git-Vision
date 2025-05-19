package org.springframework.samples.gitvision.util.graphQL.models;

import java.util.List;

import lombok.Data;

@Data
public class GraphQLTotalChangesInFile {
    private GitHubData data;

    public List<Commit> getCommits() {
        return this.getData()
                .getRepository()
                .getGitObject()
                .getBlame()
                .getRanges()
                .stream()
                .map(BlameRange::getCommit).toList();
    }

    @Data
    public static class GitHubData {
        private Repository repository;
    }

    @Data
    public static class Repository {
        private DefaultBranchRef defaultBranchRef;
        private GitObject gitObject;
    }

    @Data
    public static class DefaultBranchRef {
        private String name;
    }

    @Data
    public static class GitObject {
        private Blame blame;
    }

    @Data
    public static class Blame {
        private List<BlameRange> ranges;
    }

    @Data
    public static class BlameRange {
        private Commit commit;
    }

    @Data
    public static class Commit {
        private int additions;
        private int deletions;
    }

}
