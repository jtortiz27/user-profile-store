package com.ortiz.userprofilestore.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class ErrorHandlerController {

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<?>> handleIllegalArguments(Exception e) {
        return Mono.just(new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<?>> handleGenericException(Exception e) {
        return Mono.just(new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
