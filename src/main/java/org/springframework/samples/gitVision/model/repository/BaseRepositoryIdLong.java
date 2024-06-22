package org.springframework.samples.gitvision.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepositoryIdLong<T> extends CrudRepository<T, Long>{
    
}
