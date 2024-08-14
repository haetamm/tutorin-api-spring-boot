package tutorin.com.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tutorin.com.entities.WebResponse;
import java.util.function.Supplier;

@Component
public class Utilities {
    public <T> ResponseEntity<WebResponse<T>> handleRequest(Supplier<T> requestHandler, HttpStatus status, String message) {
        T data = requestHandler.get();
        WebResponse<T> response = new WebResponse<>(
                status.value(),
                message,
                data,
                null
        );
        return ResponseEntity.status(status).body(response);
    }
}

