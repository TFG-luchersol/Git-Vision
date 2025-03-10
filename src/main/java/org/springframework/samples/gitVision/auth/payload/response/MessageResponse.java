package org.springframework.samples.gitvision.auth.payload.response;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.util.Information;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse extends ResponseEntity<Information> {

	public MessageResponse(Information body, HttpStatusCode status) {
		super(body, status);
	}

	public static MessageResponse of(Information information, HttpStatusCode status) {
        return new MessageResponse(information, status);
    }

	public static MessageResponse of(String message, HttpStatusCode status) {
        return of(Information.of(message), status);
    }

	public static MessageResponse of(Exception exception, HttpStatusCode status) {
        return of(exception.getMessage(), status);
    }

	
}
