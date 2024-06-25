package org.springframework.samples.gitvision.auth.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(staticName = "empty")
public class MessageResponse {

	private String message;

	public MessageResponse(String message) {
		this.message = message;
	}


	public static MessageResponse of(String message){
		MessageResponse messageResponse = MessageResponse.empty();
		messageResponse.setMessage(message);
		return messageResponse;
	}
}
