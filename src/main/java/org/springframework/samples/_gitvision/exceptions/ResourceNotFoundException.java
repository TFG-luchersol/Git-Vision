package org.springframework.samples.gitvision.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends ResponseStatusException {

    public ResourceNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }

	public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(HttpStatus.NOT_FOUND, String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
	}

	public ResourceNotFoundException(final String message) {
        super(HttpStatus.NOT_FOUND, message);
	}

	public ResourceNotFoundException(final Class<?> clazz) {
        this(String.format("%s not found", clazz.getSimpleName()));
	}

    public static ResourceNotFoundException of(Class<?> clazz, String fieldName, Object fieldValue) {
        return new ResourceNotFoundException(clazz.getSimpleName(), fieldName, fieldValue);
    }

    public static ResourceNotFoundException of(String resourceName, String fieldName, Object fieldValue) {
        return new ResourceNotFoundException(resourceName, fieldName, fieldValue);
    }

    public static ResourceNotFoundException of(String message) {
        return new ResourceNotFoundException(message);
    }

    public static ResourceNotFoundException of(Class<?> clazz) {
        return new ResourceNotFoundException(clazz);
    }

}
