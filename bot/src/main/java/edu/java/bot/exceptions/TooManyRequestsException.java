package edu.java.bot.exceptions;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String cause) {
        super(cause);
    }
}
