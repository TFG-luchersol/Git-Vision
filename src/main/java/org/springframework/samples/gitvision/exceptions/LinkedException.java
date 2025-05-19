package org.springframework.samples.gitvision.exceptions;

public class LinkedException extends RuntimeException {

    public LinkedException(String message) {
        super(message);
    }

    public static LinkedException linkWorkspace() {
        return new LinkedException("Workspace linked error");
    }


    public static LinkedException linkGithub() {
        return new LinkedException("Github linked error");
    }
}
