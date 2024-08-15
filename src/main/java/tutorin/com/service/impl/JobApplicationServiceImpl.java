package tutorin.com.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorin.com.constant.Status;
import tutorin.com.entities.job_application.JobApplicationRequest;
import tutorin.com.entities.job_application.JobApplicationResponse;
import tutorin.com.entities.job_application.UpdateJobApplicationRequest;
import tutorin.com.exception.BadRequestException;
import tutorin.com.exception.NotFoundException;
import tutorin.com.model.Job;
import tutorin.com.model.JobApplication;
import tutorin.com.model.User;
import tutorin.com.repository.JobApplicationRepository;
import tutorin.com.service.JobApplicationService;
import tutorin.com.service.JobService;
import tutorin.com.service.UserService;
import tutorin.com.validation.ValidationUtil;

@Service
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements JobApplicationService {
    private final ValidationUtil validationUtil;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobService jobService;
    private final UserService userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JobApplicationResponse createJobApplication(JobApplicationRequest request) throws NotFoundException {
        validationUtil.validate(request);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUserId(userId);
        Job job = jobService.findById(request.getJobId());

        JobApplication jobApplication = JobApplication.builder()
                .job(job)
                .tutor(user)
                .status(Status.ACTIVE)
                .build();

        jobApplication = jobApplicationRepository.saveAndFlush(jobApplication);

        return JobApplicationResponse.builder()
                .jobId(jobApplication.getJob().getId())
                .tutorId(jobApplication.getTutor().getId())
                .status(String.valueOf(jobApplication.getStatus()))
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JobApplicationResponse updateJobApplication(UpdateJobApplicationRequest request) throws NotFoundException, BadRequestException {
        validationUtil.validate(request);

        Job job = jobService.findById(request.getJobId());

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!userId.equals(job.getStudent().getId())) {
            throw new AccessDeniedException("You are not authorized to update this job application");
        }

        JobApplication jobApplication = jobApplicationRepository.findByJobIdAndTutorId(job.getId(), request.getTutorId())
                .orElseThrow(() -> new NotFoundException("Job application not found"));

        Status statusToUpdate = Status.valueOf(request.getStatus());

        if (statusToUpdate == Status.ACTIVE) {
            throw new BadRequestException("Job application status cannot be updated to ACTIVE");
        }

        jobApplication.setStatus(statusToUpdate);

        jobApplicationRepository.saveAndFlush(jobApplication);

        return JobApplicationResponse.builder()
                .jobId(jobApplication.getJob().getId())
                .tutorId(jobApplication.getTutor().getId())
                .status(String.valueOf(jobApplication.getStatus()))
                .build();
    }
}