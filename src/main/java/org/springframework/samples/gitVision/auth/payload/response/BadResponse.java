package org.springframework.samples.gitvision.auth.payload.response;

import org.springframework.http.HttpStatus;
import org.springframework.samples.gitvision.util.Information;

public class BadResponse extends MessageResponse {

    public BadResponse(Information body) {
        super(body, HttpStatus.BAD_REQUEST);
    }

}
