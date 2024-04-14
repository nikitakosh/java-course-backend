package edu.java.exceptions;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String cause) {
        super(cause);
    }
}
