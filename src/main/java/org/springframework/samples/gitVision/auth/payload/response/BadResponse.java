package org.springframework.samples.gitvision.auth.payload.response;

import org.springframework.http.HttpStatus;
import org.springframework.samples.gitvision.util.Information;

public class BadResponse extends MessageResponse {

    public BadResponse(Information body) {
        super(body, HttpStatus.BAD_REQUEST);
    }

    public static BadResponse of(Information body) {
        return new BadResponse(body);
    }

    public static BadResponse of(String message) {
        return of(Information.of(message));
    }

    public static BadResponse of(Exception exception){
        return of(exception.getMessage());
    }

}
