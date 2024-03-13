package edu.java.exceptions;

public class NotExistentLinkException extends RuntimeException{
    public NotExistentLinkException(String cause) {
        super(cause);
    }
}
