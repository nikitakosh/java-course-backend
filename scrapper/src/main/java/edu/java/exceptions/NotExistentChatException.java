package edu.java.exceptions;

public class NotExistentChatException extends RuntimeException {

    public NotExistentChatException(String cause) {
        super(cause);
    }
}
