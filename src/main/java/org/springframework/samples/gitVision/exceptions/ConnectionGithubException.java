package org.springframework.samples.gitvision.exceptions;

public class ConnectionGithubException extends RuntimeException {

    public ConnectionGithubException(String message) {
        super(message);
    }
}
