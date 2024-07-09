package org.springframework.samples.gitvision.file;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends RepositoryIdLong<File>{
    
    @Query("select f from File f where f.repository.id = :repositoryId")
    List<File> getAllFilesByRepositoryId(Long repositoryId);

    @Query("select distinct(f.extension) from File f where f.repository.id = :repositoryId")
    List<String> getDistinctFilesExtensionsByRepositoryId(Long repositoryId);

}
