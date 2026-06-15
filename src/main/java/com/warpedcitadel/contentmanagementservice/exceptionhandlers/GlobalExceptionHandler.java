package com.warpedcitadel.contentmanagementservice.exceptionhandlers;


import com.warpedcitadel.contentmanagementservice.payload.GenericApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public GenericApiErrorResponse handelRunTimeException(RuntimeException runtimeException, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", runtimeException.getMessage());
        return new GenericApiErrorResponse(
                "Bad Request",
                HttpStatus.CONFLICT.value(),
                errors,
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC())
        );
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public GenericApiErrorResponse handelTooMuchContentException(IllegalArgumentException illegalArgumentException, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", illegalArgumentException.getMessage());
        return new GenericApiErrorResponse(
                "Bad Request",
                HttpStatus.BAD_REQUEST.value(),
                errors,
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC())
        );
    }
}
