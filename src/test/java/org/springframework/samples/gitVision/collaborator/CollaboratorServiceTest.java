package org.springframework.samples.gitvision.collaborator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CollaboratorServiceTest {

    @Autowired
    CollaboratorService collaboratorService;

    @Test
    void testGetAllCollaboratorsByRepositoryId() {
        
    }

    @Test
    void testGetNumChangesByCollaborator() {
        
    }
}
