package tutorin.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tutorin.com.constant.ApiUrl;
import tutorin.com.exception.NotFoundException;
import tutorin.com.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.API_USER)
@RequiredArgsConstructor
@Tag(name = "Image", description = "Image API")
public class ImageController {
    final private ImageService imageService;

    @Operation(summary = "Get image by id")
    @GetMapping("/{id}/images")
    public ResponseEntity<Resource> getById(@PathVariable String id) throws IOException, NotFoundException {
        Resource image = imageService.getById(id);
        Path filePath = image.getFile().toPath();
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        String headerValue = "attachment; filename=\"" + image.getFilename() + "\"";
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(image);
    }
}
