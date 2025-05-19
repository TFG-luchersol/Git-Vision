package org.springframework.samples.gitvision.file.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.samples.gitvision.change.model.Change;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class ChangesInFile {

    private String filePath;
    private Change change;
    private List<FileChangeDetail> FileChangeDetails;

    public static ChangesInFile of(String filePath, Change change) {
        return new ChangesInFile(filePath, change, new ArrayList<>());
    }

}
