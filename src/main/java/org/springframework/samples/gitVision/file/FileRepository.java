package org.springframework.samples.gitvision.file;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.gitvision.file.model.File;
import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends RepositoryIdLong<File>{
    
    List<File> findAllByRepository_Id(Long repositoryId);

    @Query("select distinct(f.extension) from File f where f.repository.id = :repositoryId")
    List<String> findDistinctFilesExtensionsByRepository_Id(Long repositoryId);

    Optional<File> findByPathAndRepository_Id(String path, Long repository_Id);

    List<String> findPathsByRepository_Id(Long repository_Id);
    
    boolean existsByPathAndRepository_Id(String path, Long repository_Id);
    
}
