package org.springframework.samples.gitvision.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class ExtractionException extends RuntimeException {

    public enum TypeExtraction {GITHUB, CLOCKIFY;}
	private static final long serialVersionUID = -3906338266891937036L;

	public ExtractionException(TypeExtraction typeExtraction, String fieldName) {
		super(String.format("%s Download fail with '%s'", typeExtraction, fieldName));
	}

	public ExtractionException(final String message) {
		super(message);
	}

}
