package org.springframework.samples.gitvision.file;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHRepositoryStatistics;
import org.kohsuke.github.GHTree;
import org.kohsuke.github.GHTreeEntry;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.file.model.File;
import org.springframework.samples.gitvision.file.model.PercentageLanguages;
import org.springframework.samples.gitvision.file.model.TreeFiles;
import org.springframework.samples.gitvision.file.model.TreeFiles.TreeNode;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    public PercentageLanguages getPercentageExtensionsByRespositoryId(Long repositoryId) throws IOException{
        GitHub gitHub = GitHub.connect();
        GHRepository ghRepository = gitHub.getRepositoryById(repositoryId);
        Map<String, Long> contLanguajes = ghRepository.listLanguages();
        return PercentageLanguages.of(contLanguajes);
    }

    public TreeNode getFileTreeByRepositoryId(Long repositoryId) throws IOException{
        GitHub gitHub = GitHub.connect();
        GHRepository ghRepository = gitHub.getRepositoryById(repositoryId);
        GHTree ghTree = ghRepository.getTreeRecursive("main", -1);
        List<File> files = ghTree.getTree().stream().map(entry -> new File(entry.getPath())).toList();
        return TreeFiles.buildTreeFromFilesWithCollapse(files);
    }
}
