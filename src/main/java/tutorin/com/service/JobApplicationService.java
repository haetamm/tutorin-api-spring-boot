package tutorin.com.service;

import tutorin.com.entities.job.JobResponse;
import tutorin.com.entities.job_application.JobApplicationRequest;
import tutorin.com.entities.job_application.JobApplicationResponse;
import tutorin.com.entities.job_application.ListJobApplicationResponse;
import tutorin.com.entities.job_application.UpdateJobApplicationRequest;
import tutorin.com.exception.BadRequestException;
import tutorin.com.exception.NotFoundException;

import java.util.List;

public interface JobApplicationService {
    List<ListJobApplicationResponse> getJobApplication();
    JobResponse createJobApplication(JobApplicationRequest request) throws NotFoundException, BadRequestException;
    JobApplicationResponse updateJobApplication(UpdateJobApplicationRequest request) throws NotFoundException, BadRequestException;
}
