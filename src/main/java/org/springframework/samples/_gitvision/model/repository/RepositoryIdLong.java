package org.springframework.samples.gitvision.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface RepositoryIdLong<T> extends JpaRepository<T, Long>{

}
