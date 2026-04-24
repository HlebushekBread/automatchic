package net.softloaf.automatchic.history.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<net.softloaf.automatchic.common.dto.response.ErrorResponse> handleResponseStatusException(ResponseStatusException e) {
        net.softloaf.automatchic.common.dto.response.ErrorResponse error = new net.softloaf.automatchic.common.dto.response.ErrorResponse(
                e.getStatusCode().value(),
                e.getReason()
        );

        return new ResponseEntity<>(error, e.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<net.softloaf.automatchic.common.dto.response.ErrorResponse> handleGeneralException(Exception e) {
        net.softloaf.automatchic.common.dto.response.ErrorResponse error = new net.softloaf.automatchic.common.dto.response.ErrorResponse(500, "Внутренняя ошибка сервера");
        log.error(e.getMessage());
        return ResponseEntity.internalServerError().body(error);
    }
}
