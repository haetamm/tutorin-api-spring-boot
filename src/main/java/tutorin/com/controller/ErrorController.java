package tutorin.com.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tutorin.com.constant.StatusMessages;
import tutorin.com.entities.WebErrorResponse;
import tutorin.com.exception.InternalServerException;
import tutorin.com.exception.NotFoundException;
import tutorin.com.exception.ValidationCustomException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebErrorResponse<Object>> handleConstraintViolationException(ConstraintViolationException exception) {
        List<Map<String, String>> errors = exception.getConstraintViolations().stream()
                .map(violation -> Map.of("path", violation.getPropertyPath().toString(), "message", violation.getMessage()))
                .collect(Collectors.toList());
        return createErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, errors);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<WebErrorResponse<String>> handleNotFoundException(NotFoundException exception) {
        return createErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage() != null ? exception.getMessage() : "Not Found");
    }

    @ExceptionHandler(ValidationCustomException.class)
    public ResponseEntity<WebErrorResponse<Object>> handleValidationCustomException(ValidationCustomException exception) {
        List<Map<String, String>> errors = List.of(Map.of("path", exception.getPath(), "message", exception.getMessage()));
        return createErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, errors);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<WebErrorResponse<String>> handleInternalServerException(InternalServerException exception) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage() != null ? exception.getMessage() : StatusMessages.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<WebErrorResponse<String>> handleAccessDeniedException(AccessDeniedException ex) {
        return createErrorResponse(HttpStatus.FORBIDDEN, ex != null ? ex.getMessage() : "");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<WebErrorResponse<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessage = StatusMessages.BAD_REQUEST;
        return createErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<WebErrorResponse<String>> handleIllegalStateException(IllegalStateException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, ex != null ? ex.getMessage() : "");
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<WebErrorResponse<String>> handleNullPointerException(NullPointerException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, ex != null ? ex.getMessage() : "");
    }

    private <T> ResponseEntity<WebErrorResponse<T>> createErrorResponse(HttpStatus status, T data) {
        WebErrorResponse<T> response = new WebErrorResponse<>();
        response.setCode(status.value());
        response.setStatus(status.name());
        response.setMessage(data);
        return ResponseEntity.status(status).body(response);
    }
}
