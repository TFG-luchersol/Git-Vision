package org.springframework.samples.gitvision.collaborator.model.changesByCollaborator;

import java.util.Collection;

import org.springframework.samples.gitvision.util.Container;

import lombok.Getter;

@Getter
public class ChangesByCollaborator extends Container<ChangesByCollaboratorsItem> {

    public ChangesByCollaborator(Collection<Object[]> collection) {
        super(collection);
    }

    public static ChangesByCollaborator of(Collection<Object[]> collection){
        return new ChangesByCollaborator(collection);
    }

}

