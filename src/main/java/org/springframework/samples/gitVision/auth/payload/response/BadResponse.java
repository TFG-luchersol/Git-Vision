package org.springframework.samples.gitvision.auth.payload.response;

import org.springframework.http.HttpStatus;

public class BadResponse extends MessageResponse {
    
    public BadResponse(){
		super(HttpStatus.BAD_REQUEST);
    }

    public BadResponse(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public static BadResponse of(Exception exception) {
        return new BadResponse(exception.getMessage());
    }

    public static BadResponse of(String message){
        return new BadResponse(message);
	}

}
