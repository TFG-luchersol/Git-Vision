package org.springframework.samples.gitvision.file;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.file.model.File;
import org.springframework.samples.gitvision.file.model.PercentageLanguages;
import org.springframework.samples.gitvision.file.model.TreeFiles;
import org.springframework.samples.gitvision.file.model.TreeFiles.TreeNode;
import org.springframework.stereotype.Service;

@Service
public class FileService {
    
    @Autowired
    FileRepository fileRepository;

    public PercentageLanguages getPercentageExtensionsByRespositoryId(Long repositoryId){
        List<File> files = fileRepository.getAllFilesByRepositoryId(repositoryId);
        return PercentageLanguages.of(files);
    }

    public TreeNode getFileTreeByRepositoryId(Long repositoryId){
        List<File> files = fileRepository.getAllFilesByRepositoryId(repositoryId);
        return TreeFiles.buildTreeFromFilesWithCollapse(files);
    }
}
