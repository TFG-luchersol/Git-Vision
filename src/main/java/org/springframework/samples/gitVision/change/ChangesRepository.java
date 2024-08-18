package org.springframework.samples.gitvision.change;

import org.springframework.samples.gitvision.change.model.Change;
import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;

public interface ChangesRepository extends RepositoryIdLong<Change> {
    
}
