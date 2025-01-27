package tutorin.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tutorin.com.constant.ApiUrl;
import tutorin.com.constant.StatusMessages;
import tutorin.com.entities.PaginationResponse;
import tutorin.com.entities.WebResponse;
import tutorin.com.entities.job.JobRequest;
import tutorin.com.entities.job.JobResponse;
import tutorin.com.exception.NotFoundException;
import tutorin.com.helper.Utilities;
import tutorin.com.service.JobService;

import java.util.List;

@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.API_JOB)
@RequiredArgsConstructor
@Tag(name = "Job", description = "Job API")
public class JobController {
    private final Utilities utilities;
    private final JobService jobService;


    @Operation(summary = "Super admin and tutor get all Job")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_TUTOR', 'ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<JobResponse>>> getAllJob(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        Page<JobResponse> jobPage = jobService.getAllJob(page, size);

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .totalPages(jobPage.getTotalPages())
                .totalElements(jobPage.getTotalElements())
                .page(page)
                .size(size)
                .hasNext(jobPage.hasNext())
                .hasPrevious(jobPage.hasPrevious())
                .build();

        return utilities.handleRequest(jobPage::getContent, HttpStatus.OK, StatusMessages.SUCCESS_RETRIEVE_LIST, paginationResponse);
    }



    @Operation(summary = "Student create job")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<JobResponse>> createJob(@RequestBody JobRequest request) {
        return utilities.handleRequest(() -> {
            try {
                return jobService.createJob(request);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }, HttpStatus.CREATED, "Request tutor successfully");
    }

    @Operation(summary = "Super admin and tutor get job by id")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_TUTOR', 'ROLE_ADMIN')")
    @GetMapping( path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<JobResponse>> getJobById(@PathVariable String id) {
        return utilities.handleRequest(() -> {
            try {
                return jobService.getJobById(id);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }, HttpStatus.OK, StatusMessages.SUCCESS_RETRIEVE);
    }

}
