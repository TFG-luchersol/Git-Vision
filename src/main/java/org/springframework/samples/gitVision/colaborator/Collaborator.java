package org.springframework.samples.gitvision.colaborator;

import org.springframework.samples.gitvision.model.entity.Person;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "collaborators")
public class Collaborator extends Person {
    
}
