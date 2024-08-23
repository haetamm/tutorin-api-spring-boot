package tutorin.com.service;

import org.springframework.http.ResponseEntity;
import tutorin.com.entities.WebResponse;
import tutorin.com.entities.job.JobRequest;
import tutorin.com.entities.job.JobResponse;
import tutorin.com.exception.NotFoundException;
import tutorin.com.model.Job;

import java.util.List;

public interface JobService {
    JobResponse createJob(JobRequest request) throws NotFoundException;
    JobResponse getJobById(String id) throws NotFoundException;
    ResponseEntity<WebResponse<List<JobResponse>>> getAllJob(Integer page, Integer size);
    Job findById(String id) throws NotFoundException;
}
