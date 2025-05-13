package org.springframework.samples.gitvision.exceptions;

public class ExistingRelationException extends RuntimeException {

    public ExistingRelationException(String message) {
        super(message);
    }

}
