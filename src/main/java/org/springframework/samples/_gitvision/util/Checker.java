package org.springframework.samples.gitvision.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ResponseStatusException;

public class Checker {

    public static void check(Boolean condition, HttpStatusCode httpStatusCode, String reason){
        if(!condition) {
            throw new ResponseStatusException(httpStatusCode, reason);
        }
    }

    public static void checkOrForbidden(Boolean condition, String message){
        check(condition, HttpStatus.FORBIDDEN, message);
    }

    public static void checkOrForbidden(Boolean condition){
        checkOrForbidden(condition, "No permitido");
    }

    public static void checkOrBadRequest(Boolean condition, String message){
        check(condition, HttpStatus.BAD_REQUEST, message);
    }

    public static void checkOrBadRequest(Boolean condition){
        check(condition, HttpStatus.BAD_REQUEST, "message");
    }

    public static <T extends Exception> void checkOrThrow(Boolean condition, T exception) throws T {
        if(!condition) {
            throw exception;
        }
    }

    public static void checkOrIllegalException(Boolean condition, String message) throws Exception {
        checkOrThrow(condition, new IllegalArgumentException(message));
    }

    public static void checkBindingResult(BindingResult result) throws IllegalAccessException {
        if (result.hasErrors()) {
            FieldError firstError = result.getFieldErrors().get(0);
            String field = firstError.getField().substring(0, 1).toUpperCase() +
                            firstError.getField().substring(1).toLowerCase(),
                    defaultMessage = firstError.getDefaultMessage(),
                    message = field + " " + defaultMessage;
            throw new IllegalAccessException(message);
        }
    }
}
