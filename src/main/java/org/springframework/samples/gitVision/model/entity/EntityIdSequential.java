package org.springframework.samples.gitvision.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;

@MappedSuperclass
public class EntityIdSequential {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_sequence")
    @SequenceGenerator(name = "global_sequence", sequenceName = "git_vision.global_sequence", allocationSize = 1)
	protected Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonIgnore
	public boolean isNew() {
		return this.id == null;
	}

}
