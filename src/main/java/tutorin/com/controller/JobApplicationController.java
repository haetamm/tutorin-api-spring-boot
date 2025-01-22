package tutorin.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tutorin.com.constant.ApiUrl;
import tutorin.com.constant.StatusMessages;
import tutorin.com.entities.WebResponse;
import tutorin.com.entities.job.JobResponse;
import tutorin.com.entities.job_application.JobApplicationRequest;
import tutorin.com.entities.job_application.JobApplicationResponse;
import tutorin.com.entities.job_application.ListJobApplicationResponse;
import tutorin.com.entities.job_application.UpdateJobApplicationRequest;
import tutorin.com.exception.BadRequestException;
import tutorin.com.exception.NotFoundException;
import tutorin.com.helper.Utilities;
import tutorin.com.service.JobApplicationService;

import java.util.List;

@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.API_JOB_APPLICATION)
@RequiredArgsConstructor
@Tag(name = "Job Application", description = "Job Application API")
public class JobApplicationController {
    private final Utilities utilities;
    private final JobApplicationService jobApplicationService;

    @Operation(summary = "Tutor get job application")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_TUTOR')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<ListJobApplicationResponse>>> getJobApplication() {
        return utilities.handleRequest(jobApplicationService::getJobApplication, HttpStatus.OK, StatusMessages.SUCCESS_RETRIEVE_LIST);
    }

    @Operation(summary = "Tutor create job application (apply job)")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_TUTOR')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<JobResponse>> createJob(@RequestBody JobApplicationRequest request) {
        return utilities.handleRequest(() -> {
            try {
                return jobApplicationService.createJobApplication(request);
            } catch (NotFoundException | BadRequestException e) {
                throw new RuntimeException(e);
            }
        }, HttpStatus.CREATED, "Application submitted successfully.");
    }

    @Operation(summary = "Student update status job application")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT')")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<JobApplicationResponse>> studentUpdateApplicationJob(@RequestBody UpdateJobApplicationRequest request) {
        return utilities.handleRequest(() -> {
            try {
                return jobApplicationService.updateJobApplication(request);
            } catch (NotFoundException | BadRequestException e) {
                throw new RuntimeException(e);
            }
        }, HttpStatus.CREATED, "Application submitted successfully.");
    }
}
