package edu.java.exceptions;

import edu.java.controllers.dto.ApiErrorResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    public static final String ERROR_DESCRIPTION = "Некорректные параметры запроса";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler()
    public ApiErrorResponse handleNotExistentChatException(NotExistentChatException ex) {
        return new ApiErrorResponse(
                "Чат не существует",
                HttpStatus.NOT_FOUND.toString(),
                "NotExistentChatException",
                ex.getMessage(),
                List.of()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiErrorResponse handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        return new ApiErrorResponse(
                "пропущены требуемые заголовки запроса",
                HttpStatus.BAD_REQUEST.toString(),
                "MissingRequestHeaderException",
                ex.getMessage(),
                List.of()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiErrorResponse handleFieldException(MethodArgumentNotValidException ex) {

        return new ApiErrorResponse(
                ERROR_DESCRIPTION,
                ex.getStatusCode().toString(),
                ex.getTitleMessageCode(),
                ex.getMessage(),
                List.of(ex.getSuppressedFields())
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiErrorResponse handleEmptyRequestBodyException(HttpMessageNotReadableException ex) {

        return new ApiErrorResponse(
                ERROR_DESCRIPTION,
                HttpStatus.BAD_REQUEST.toString(),
                ex.toString(),
                ex.getMessage(),
                List.of()
        );
    }
}
