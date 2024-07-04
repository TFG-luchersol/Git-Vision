package org.springframework.samples.gitvision.auth.payload.response;

import org.springframework.samples.gitvision.util.Data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(staticName = "empty")
public class MessageResponse {

	private String message;
	private Data data;

	public MessageResponse(String message) {
		this.message = message;
	}

	public static MessageResponse of(String message){
		MessageResponse messageResponse = MessageResponse.empty();
		messageResponse.setMessage(message);
		return messageResponse;
	}

	public static MessageResponse of(Data data){
		MessageResponse messageResponse = MessageResponse.empty();
		messageResponse.setData(data);
		return messageResponse;
	}

	public static MessageResponse of(String message, Data data){
		MessageResponse messageResponse = of(message);
		messageResponse.setData(data);
		return messageResponse;
	}
}
