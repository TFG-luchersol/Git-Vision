package org.springframework.samples.gitvision.file;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTree;
import org.springframework.samples.gitvision.change.model.Change;
import org.springframework.samples.gitvision.file.model.ChangesInFile;
import org.springframework.samples.gitvision.file.model.File;
import org.springframework.samples.gitvision.file.model.PercentageLanguages;
import org.springframework.samples.gitvision.file.model.TreeFiles;
import org.springframework.samples.gitvision.file.model.TreeFiles.TreeNode;
import org.springframework.samples.gitvision.util.EntityUtils;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    public PercentageLanguages getPercentageExtensionsByRespositoryName(GHRepository ghRepository) throws Exception{
        Map<String, Long> contLanguajes = ghRepository.listLanguages();
        return PercentageLanguages.of(contLanguajes);
    }

    public byte[] getFileContentTreeByPath(GHRepository ghRepository, String path) throws Exception {
        GHContent ghContent = ghRepository.getFileContent(path);
        return ghContent.read().readAllBytes();
    }

    public TreeNode getFileTreeByRepositoryName(GHRepository ghRepository) throws Exception {
        GHTree ghTree = ghRepository.getTreeRecursive(ghRepository.getDefaultBranch(), -1);
        List<String> paths = ghTree.getTree().stream().map(entry -> entry.getPath()).toList();
        return TreeFiles.buildTreeWithCollapse(paths);
    }

    public TreeNode getFileSubTreeByRepositoryName(GHRepository ghRepository, String initPath) throws Exception {
        GHTree ghTree = ghRepository.getTreeRecursive(ghRepository.getDefaultBranch(), -1);
        List<String> paths = ghTree.getTree().stream()
                                             .map(entry -> entry.getPath())
                                             .filter(path -> path.startsWith(initPath) && !path.equals(initPath))
                                             .toList();
        return TreeFiles.buildTreeWithCollapse(paths);
    }

    public Map<String, Long> getExtensionCounterByRepositoryId(GHRepository ghRepository) throws Exception{
        GHTree ghTree = ghRepository.getTreeRecursive(ghRepository.getDefaultBranch(), -1);
        Map<String, Long> cont = ghTree.getTree().stream()
                               .filter(i -> "blob".equals(i.getType()))
                               .map(entry -> new File(entry.getPath()).getExtension())
                               .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return cont;
    }

    // public List<ChangesInFile> getChangesInFilesByUser(String repositoryName, String author, String login) throws IOException {
    //     GVRepo gvRepo = this.gvRepoRepository.findByNameAndUser_Username(repositoryName, login)
    //         .orElseThrow(() -> new ResourceNotFoundException("Not found repository"));
    //     String tokenToUse = gvRepo.getToken();
    //     GithubGraphQLApi githubGraphQLApi = GithubGraphQLApi.connect(tokenToUse);
    //     List<ChangesInFile> changesInFiles = githubGraphQLApi.getChangesInFilesByUser(repositoryName, author);
    //     return changesInFiles;
    // }

    public List<ChangesInFile> getChangesInFilesByUser(GHRepository ghRepository, String author, LocalDate startDate, LocalDate endDate) throws Exception{
        Map<String, Change> fileChangesMap = new HashMap<>();
        var ghCommits = ghRepository.queryCommits().author(author);
        if(Objects.nonNull(startDate)) ghCommits = ghCommits.since(EntityUtils.parseLocalDateUTCToDate(startDate));
        if(Objects.nonNull(endDate)) ghCommits = ghCommits.until(EntityUtils.parseLocalDateUTCToDate(endDate));

        for (GHCommit ghCommit : ghCommits.list()) {
            ghCommit.listFiles().forEach(file -> {
                String filePath = file.getFileName();
                int additions = file.getLinesAdded();
                int deletions = file.getLinesDeleted();

                fileChangesMap
                        .computeIfAbsent(filePath, _ -> new Change())
                        .addChanges(additions, deletions);
            });
        }

        return fileChangesMap.entrySet()
                    .stream()
                    .map(entry -> ChangesInFile.of(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
    }

}
