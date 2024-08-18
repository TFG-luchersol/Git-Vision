package org.springframework.samples.gitvision.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface RepositoryIdString<T> extends CrudRepository<T,String> {
 
}
