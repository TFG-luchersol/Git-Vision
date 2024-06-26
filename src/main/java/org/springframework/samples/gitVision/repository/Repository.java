package org.springframework.samples.gitvision.repository;

import java.util.List;

import org.springframework.samples.gitvision.colaborator.Collaborator;
import org.springframework.samples.gitvision.model.entity.EntityIdLong;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "repositories")
public class Repository extends EntityIdLong {

    private String name;

}
