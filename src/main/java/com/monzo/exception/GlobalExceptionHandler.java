package com.monzo.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.MalformedURLException;
import java.util.concurrent.CompletionException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${malformed-url-error-message}")
    private String malformedUrlMessage;

    @ExceptionHandler({
            MalformedURLException.class,
            IllegalArgumentException.class,
            CompletionException.class
    })
    public ResponseEntity<ExceptionResponse> handleMalformedURLException() {
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), malformedUrlMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleCommonException(Exception e) {
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
