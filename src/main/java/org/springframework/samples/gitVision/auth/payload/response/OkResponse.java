package org.springframework.samples.gitvision.auth.payload.response;

import org.springframework.http.HttpStatus;
import org.springframework.samples.gitvision.util.Information;

public class OkResponse extends MessageResponse{

	public OkResponse(Information body) {
        super(body, HttpStatus.OK);
    }

    public static OkResponse of(Information body) {
        return new OkResponse(body);
    }

    public static OkResponse of(String message) {
        return of(Information.of(message));
    }
}
