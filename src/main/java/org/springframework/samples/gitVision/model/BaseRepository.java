package org.springframework.samples.gitVision.model;

import org.springframework.data.repository.CrudRepository;

public interface BaseRepository<T> extends CrudRepository<T, String>{
    
}
