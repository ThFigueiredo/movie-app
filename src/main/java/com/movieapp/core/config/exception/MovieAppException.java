package com.movieapp.core.config.exception;

public class MovieAppException extends RuntimeException {

    public MovieAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
