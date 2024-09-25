package org.springframework.samples.gitvision.file.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class File {

    public File(String path){
        this.path = path;
        this.calcExtension();
    }

    String path;

    String extension;

    public void setPath(String path) {
        this.path = path;
        this.calcExtension();
    }

    private void calcExtension() {
        if (this.path == null) {
            this.extension = null;
        } else {
            int dotIndex = this.path.lastIndexOf(".");
            if (dotIndex <= 0 || dotIndex == this.path.length() - 1)
                this.extension = null;
            else
                this.extension = this.path.substring(dotIndex + 1);
        }

    }

    public String getFileName() {
        int slashIndex = this.path.lastIndexOf("/");
        return this.path.substring(slashIndex + 1);
    }

}
