package org.springframework.samples.gitvision.file;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FileServiceTest {

    @Autowired
    FileService fileService;

    private static final Long ID_REPOSITORY = 1L;

    @Test
    void testGetPercentageExtensionsByRespositoryId() {
        PercentageLanguages percentageLanguages = fileService.getPercentageExtensionsByRespositoryId(ID_REPOSITORY);
        assertEquals(2, percentageLanguages.get("Java"));
        assertEquals(1, percentageLanguages.get("Python"));
        assertEquals(1, percentageLanguages.get("C"));
        assertEquals(2, percentageLanguages.get("C++"));
        assertEquals(0, percentageLanguages.get("CSS"));
        assertEquals(1, percentageLanguages.getUnknown());
    }
}
