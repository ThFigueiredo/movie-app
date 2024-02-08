package com.movieapp.core.config.exception;

public class CsvReadException extends RuntimeException {

    public CsvReadException(String message) {
        super(message);
    }

    public CsvReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
