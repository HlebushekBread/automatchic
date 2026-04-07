package net.softloaf.automatchic.app.exception;

import net.softloaf.automatchic.app.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException e) {
        ErrorResponse error = new ErrorResponse(
                e.getStatusCode().value(),
                e.getReason()
        );

        return new ResponseEntity<>(error, e.getStatusCode());
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(InternalAuthenticationServiceException e) {
        String input = e.getMessage();

        int status = 500;
        String message = "Внутренняя ошибка аутентификации";

        if (input != null && !input.isEmpty()) {
            Pattern pattern = Pattern.compile("^(\\d+).*?\"(.+)\"$");
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                status = Integer.parseInt(matcher.group(1));
                message = matcher.group(2) + " (Внутренняя ошибка аутентификации)";
            }
        }

        ErrorResponse error = new ErrorResponse(status, message);
        HttpStatus httpStatus;
        try {
            httpStatus = HttpStatus.valueOf(status);
        } catch (IllegalArgumentException iae) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        ErrorResponse error = new ErrorResponse(500, "Внутренняя ошибка сервера");
        return ResponseEntity.internalServerError().body(error);
    }
}
