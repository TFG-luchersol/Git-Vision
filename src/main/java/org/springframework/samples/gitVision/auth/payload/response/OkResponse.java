package org.springframework.samples.gitvision.auth.payload.response;

import org.springframework.http.HttpStatus;
import org.springframework.samples.gitvision.util.Information;

public class OkResponse extends MessageResponse{

    public OkResponse(){
		super(HttpStatus.OK);
    }
    
    public OkResponse(String message) {
        super(message, HttpStatus.OK);
    }

    public static OkResponse of(String message){
		return new OkResponse(message);
	}

	public static OkResponse of(Information information){
		OkResponse messageResponse = new OkResponse();
		messageResponse.setBody(information);
		return messageResponse;
	}

	public static OkResponse of(String message, Information information){
		OkResponse messageResponse = of(message);
		messageResponse.setBody(information);
		return messageResponse;
	}

}
