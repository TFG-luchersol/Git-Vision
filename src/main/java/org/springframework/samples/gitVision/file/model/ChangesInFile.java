package org.springframework.samples.gitvision.file.model;

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

    public static ChangesInFile of(String filePath, Change change) {
        return new ChangesInFile(filePath, change);
    }

}
