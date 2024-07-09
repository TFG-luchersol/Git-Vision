package org.springframework.samples.gitvision.file;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.util.LanguageDetector;
import org.springframework.stereotype.Service;

@Service
public class FileService {
    
    @Autowired
    FileRepository fileRepository;

    public PercentageLanguages getPercentageExtensionsByRespositoryId(Long respositoryId){
        List<File> files = fileRepository.getAllFilesByRepositoryId(respositoryId);
        PercentageLanguages percentageLanguages = PercentageLanguages.empty();
        for (File file : files) {
            String language = LanguageDetector.detectLanguageByExtension(file);
            percentageLanguages.add(language);
        }
        return percentageLanguages;
    }
}
