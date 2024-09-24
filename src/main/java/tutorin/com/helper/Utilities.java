package tutorin.com.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tutorin.com.entities.PaginationResponse;
import tutorin.com.entities.WebResponse;
import tutorin.com.entities.file.FileResponse;
import tutorin.com.model.Image;
import tutorin.com.model.Resume;

import java.security.SecureRandom;
import java.util.function.Supplier;

@Component
public class Utilities {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static FileResponse createResponseFile(Image image) {
        if (image == null) { return null; }
        return createResponse(image.getId(), image.getName(), image.getPath());
    }

    public static FileResponse createResponseFile(Resume resume) {
        if (resume == null) { return null; }
        return createResponse(resume.getId(), resume.getName(), resume.getPath());
    }

    private static FileResponse createResponse(String id, String name, String path) {
        return FileResponse.builder()
                .id(id)
                .name(name)
                .path(path)
                .build();
    }
    
    public <T> ResponseEntity<WebResponse<T>> handleRequest(Supplier<T> requestHandler, HttpStatus status, String message, PaginationResponse paginationResponse) {
        T data = requestHandler.get();
        WebResponse<T> response = new WebResponse<>(
                status.value(),
                message,
                data,
                paginationResponse
        );
        return ResponseEntity.status(status).body(response);
    }

    public <T> ResponseEntity<WebResponse<T>> handleRequest(Supplier<T> requestHandler, HttpStatus status, String message) {
        return handleRequest(requestHandler, status, message, null);
    }
}


