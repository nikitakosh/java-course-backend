package edu.java.exceptions;

import edu.java.controllers.dto.ApiErrorResponse;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ApiErrorResponse handleNotExistentChatException(NotExistentChatException ex) {
        return new ApiErrorResponse(
                "chat does not exist",
                HttpStatus.NOT_FOUND.toString(),
                "NotExistentChatException",
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiErrorResponse handleChatAlreadyExistException(ChatAlreadyExistException ex) {
        return new ApiErrorResponse(
                "chat already exist",
                HttpStatus.BAD_REQUEST.toString(),
                "ChatAlreadyExistException",
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiErrorResponse handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        return new ApiErrorResponse(
                "Missing required request headers",
                HttpStatus.BAD_REQUEST.toString(),
                "MissingRequestHeaderException",
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiErrorResponse handleFieldException(MethodArgumentNotValidException ex) {
        return new ApiErrorResponse(
                "invalid field value",
                HttpStatus.BAD_REQUEST.toString(),
                ex.getTitleMessageCode(),
                "MethodArgumentNotValidException",
                Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiErrorResponse handleEmptyRequestBodyException(HttpMessageNotReadableException ex) {
        return new ApiErrorResponse(
                "request body missing",
                HttpStatus.BAD_REQUEST.toString(),
                "HttpMessageNotReadableException",
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiErrorResponse handleLinkAlreadyTrackException(LinkAlreadyTrackException ex) {
        return new ApiErrorResponse(
                "link already tracked",
                HttpStatus.BAD_REQUEST.toString(),
                "LinkAlreadyTrackException",
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiErrorResponse handleLinkWasNotTrackedException(LinkWasNotTrackedException ex) {
        return new ApiErrorResponse(
                "link was not tracked",
                HttpStatus.BAD_REQUEST.toString(),
                "LinkWasNotTrackedException",
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }
}
