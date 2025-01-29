package org.springframework.samples.gitvision.file;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTree;
import org.springframework.samples.gitvision.change.model.Change;
import org.springframework.samples.gitvision.file.model.ChangesByUser;
import org.springframework.samples.gitvision.file.model.File;
import org.springframework.samples.gitvision.file.model.PercentageLanguages;
import org.springframework.samples.gitvision.file.model.TreeFiles;
import org.springframework.samples.gitvision.file.model.TreeFiles.TreeNode;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    public PercentageLanguages getPercentageExtensionsByRespositoryName(GHRepository ghRepository) throws Exception{
        Map<String, Long> contLanguajes = ghRepository.listLanguages();
        return PercentageLanguages.of(contLanguajes);
    }

    public String getFileContentTreeByPath(GHRepository ghRepository, String path) throws Exception {
        GHContent ghContent = ghRepository.getFileContent(path);
        return new String(ghContent.read().readAllBytes());
    }

    public TreeNode getFileTreeByRepositoryName(GHRepository ghRepository) throws Exception {
        GHTree ghTree = ghRepository.getTreeRecursive(ghRepository.getDefaultBranch(), -1);
        List<String> paths = ghTree.getTree().stream().map(entry -> entry.getPath()).toList();
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

    public ChangesByUser getChangeByUserInPath(GHRepository ghRepository, String path) throws Exception{
        ChangesByUser changesByUser = new ChangesByUser();
        for (GHCommit ghCommit : ghRepository.queryCommits().path(path).list()) {
            for(var file: ghCommit.listFiles()){

                // Hay que comprobar si un archivo anterior tenía el mismo nombre, 
                // ya que en dicho caso puede suceder que este haya sido renombrado
                if(file.getPreviousFilename() != null && file.getPreviousFilename().equals(path)){
                    innerGetChangeByUserInPath(changesByUser, ghRepository, path);
                    break;
                } else if(path.equals(file.getFileName())){
                    Change change = Change.of(file.getLinesAdded(), file.getLinesDeleted());
                    changesByUser.add(ghCommit.getAuthor().getLogin(), change);
                    break;
                }
            }
        }

        return changesByUser;
    }

    private void innerGetChangeByUserInPath(ChangesByUser changesByUser, GHRepository ghRepository, String path) throws IOException{        
        for (GHCommit ghCommit : ghRepository.queryCommits().path(path).list()) {
            for(GHCommit.File file: ghCommit.listFiles()){
                if(file.getPreviousFilename() != null && file.getPreviousFilename().equals(path)){
                    innerGetChangeByUserInPath(changesByUser, ghRepository, path);
                    break;
                } else if(path.equals(file.getFileName())){
                    Change change = Change.of(file.getLinesAdded(), file.getLinesDeleted());
                    changesByUser.add(ghCommit.getAuthor().getLogin(), change);
                    break;
                }
            }
        }
    }

}
