package com.okuylu_back.utils.exceptions;

import org.springframework.http.HttpStatus;

public class PersonException extends RuntimeException {

    private final HttpStatus httpStatus;

    public PersonException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
