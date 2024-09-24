package tutorin.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tutorin.com.constant.ApiUrl;
import tutorin.com.exception.NotFoundException;
import tutorin.com.service.ResumeService;

import java.io.IOException;

@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.API_USER)
@RequiredArgsConstructor
@Tag(name = "Resume", description = "Resume API")
public class ResumeController {
    final private ResumeService resumeService;

    @Operation(summary = "Get resume by id")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_TUTOR')")
    @GetMapping("/{id}/resume")
    public ResponseEntity<Resource> getById(
            @PathVariable String id,
            @RequestParam(required = false) String jobId,
            @RequestParam(required = false) String tutorId
    ) throws IOException, NotFoundException {
        Resource resume = resumeService.getById(id, jobId, tutorId);
        String contentType = MediaType.APPLICATION_PDF_VALUE;

        String headerValue = "attachment; filename=\"" + resume.getFilename() + "\"";
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resume);
    }
}
