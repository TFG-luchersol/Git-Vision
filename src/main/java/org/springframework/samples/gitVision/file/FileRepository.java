package org.springframework.samples.gitvision.file;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.gitvision.file.model.File;
import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends RepositoryIdLong<File>{
    
    @Query("select f from File f where f.repository.id = :repositoryId")
    List<File> findAllFilesByRepository_Id(Long repositoryId);

    @Query("select distinct(f.extension) from File f where f.repository.id = :repositoryId")
    List<String> findDistinctFilesExtensionsByRepository_Id(Long repositoryId);

    @Query("select f from File f where f.path = :path")
    File findByPath(String path);
    
}
