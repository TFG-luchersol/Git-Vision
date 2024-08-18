package org.springframework.samples.gitvision.auth.payload.response;

import org.springframework.samples.gitvision.util.Information;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(staticName = "empty")
public class MessageResponse {

	private String message;
	private Information information;

	public MessageResponse(String message) {
		this.message = message;
	}

	public static MessageResponse of(String message){
		MessageResponse messageResponse = MessageResponse.empty();
		messageResponse.setMessage(message);
		return messageResponse;
	}

	public static MessageResponse of(Information information){
		MessageResponse messageResponse = MessageResponse.empty();
		messageResponse.setInformation(information);
		return messageResponse;
	}

	public static MessageResponse of(String message, Information information){
		MessageResponse messageResponse = of(message);
		messageResponse.setInformation(information);
		return messageResponse;
	}
}
