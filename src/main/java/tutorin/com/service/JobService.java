package tutorin.com.service;

import tutorin.com.entities.job.JobRequest;
import tutorin.com.entities.job.JobResponse;
import tutorin.com.exception.NotFoundException;

import java.util.List;

public interface JobService {
    JobResponse createJob(JobRequest request) throws NotFoundException;
    JobResponse getJobById(String id) throws NotFoundException;
    List<JobResponse> getAllJob();
}
