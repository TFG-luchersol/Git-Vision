package org.springframework.samples.gitvision.file.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class File {

    private static String UNKNOWN_EXTENSION = "Unknown";

    public File(String path){
        this.path = path;
        this.calcExtension();
    }

    String path;

    String extension;

    public void setPath(String path) {
        this.path = path.trim();
        this.calcExtension();
    }

    private void calcExtension() {
        if (this.path == null) {
            this.extension = UNKNOWN_EXTENSION;
        } else {
            int dotIndex = this.getFileName().lastIndexOf(".");
            if (dotIndex <= 1 || dotIndex == this.getFileName().length() - 1)
                this.extension = UNKNOWN_EXTENSION;
            else
                this.extension = this.getFileName().substring(dotIndex + 1);
        }

    }

    public String getFileName() {
        int slashIndex = this.path.lastIndexOf("/");
        return this.path.substring(slashIndex + 1);
    }

}
