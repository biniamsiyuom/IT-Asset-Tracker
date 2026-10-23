
package IT.Asset.Tracker;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> createErrorResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "message", message,
                "path", request.getRequestURI()
        );

        return ResponseEntity.status(status).body(body);
    }

    // Invalid operation, such as assigning an unavailable asset
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(
            IllegalStateException exception,
            HttpServletRequest request) {

        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request
        );
    }

    // Duplicate serial number, category, or other conflicting value
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException exception,
            HttpServletRequest request) {

        return createErrorResponse(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                request
        );
    }

    // Request body validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": "
                        + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        if (message.isBlank()) {
            message = "Request validation failed";
        }

        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                message,
                request
        );
    }

    // Invalid request parameters, such as status=INVALID
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request) {

        String message = "Invalid value for parameter '"
                + exception.getName() + "'";

        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                message,
                request
        );
    }

    // Malformed JSON or invalid enum values in a request body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleUnreadableMessage(
            HttpMessageNotReadableException exception,
            HttpServletRequest request) {

        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid request body or field value",
                request
        );
    }

    // Missing asset, category, or user
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException exception,
            HttpServletRequest request) {

        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage() == null
                        ? "Resource not found"
                        : exception.getMessage(),
                request
        );
    }
}
