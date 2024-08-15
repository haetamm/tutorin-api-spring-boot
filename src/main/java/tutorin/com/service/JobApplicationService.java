package tutorin.com.service;

import tutorin.com.entities.job_application.JobApplicationRequest;
import tutorin.com.entities.job_application.JobApplicationResponse;
import tutorin.com.entities.job_application.UpdateJobApplicationRequest;
import tutorin.com.exception.BadRequestException;
import tutorin.com.exception.NotFoundException;

public interface JobApplicationService {
    JobApplicationResponse createJobApplication(JobApplicationRequest request) throws NotFoundException;
    JobApplicationResponse updateJobApplication(UpdateJobApplicationRequest request) throws NotFoundException, BadRequestException;
}
