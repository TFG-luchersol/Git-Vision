package org.springframework.samples.gitvision.util;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.util.MultiValueMap;

public class GeneralResponse extends ResponseEntity<MessageResponse> {

    public GeneralResponse(MessageResponse body, MultiValueMap<String, String> headers, HttpStatusCode status) {
        super(body, headers, status);
    }

    public String getMessage(){
        MessageResponse messageResponse = this.getBody();
        return messageResponse == null ? null : messageResponse.getMessage();
    }

    public Information getInformation(){
        MessageResponse messageResponse = this.getBody();
        return messageResponse == null ? null : messageResponse.getInformation();
    }
    
}
