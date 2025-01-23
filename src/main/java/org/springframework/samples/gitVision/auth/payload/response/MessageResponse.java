package org.springframework.samples.gitvision.auth.payload.response;

import org.springframework.http.HttpStatus;
import org.springframework.samples.gitvision.util.Information;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {

	protected String message;
	protected Information body;
	protected HttpStatus httpStatus;

	public MessageResponse() {

	}

	public MessageResponse(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public MessageResponse(String message) {
		this.message = message;
	}

	public MessageResponse(String message, HttpStatus httpStatus) {
		this.message = message;
		this.httpStatus = httpStatus;
	}

	public static MessageResponse of(String message){
		MessageResponse messageResponse = new MessageResponse();
		messageResponse.setMessage(message);
		return messageResponse;
	}

	public static MessageResponse of(Information information){
		MessageResponse messageResponse = new MessageResponse();
		messageResponse.setBody(information);
		return messageResponse;
	}

	public static MessageResponse of(String message, Information information){
		MessageResponse messageResponse = of(message);
		messageResponse.setBody(information);
		return messageResponse;
	}
}
