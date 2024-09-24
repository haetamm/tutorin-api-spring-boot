package tutorin.com.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorin.com.constant.Status;
import tutorin.com.constant.StatusMessages;
import tutorin.com.entities.job_application.JobApplicationRequest;
import tutorin.com.entities.job_application.JobApplicationResponse;
import tutorin.com.entities.job_application.ListJobApplicationResponse;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements JobApplicationService {
    private final ValidationUtil validationUtil;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobService jobService;
    private final UserService userService;

    @Override
    public List<ListJobApplicationResponse> getJobApplication() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<JobApplication> jobApplications = jobApplicationRepository.findAllByTutorId(userId);
        return jobApplications.stream()
                .map(this::createListJobApplicationResponse)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JobApplicationResponse createJobApplication(JobApplicationRequest request) throws NotFoundException, BadRequestException {
        validationUtil.validate(request);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUserId(userId);

        if (user.getResume() == null) {
            throw new BadRequestException("Please upload your resume first");
        }

        Job job = jobService.findById(request.getJobId());
        User student = userService.getByUserId(job.getStudent().getId());

        Optional<JobApplication> existingApplication = jobApplicationRepository.findByJobIdAndTutorId(job.getId(), user.getId());
        if (existingApplication.isPresent()) {
            throw new BadRequestException("You have already applied for this job.");
        }

        JobApplication jobApplication = JobApplication.builder()
                .job(job)
                .tutor(user)
                .student(student)
                .status(Status.ACTIVE)
                .build();

        jobApplication = jobApplicationRepository.saveAndFlush(jobApplication);

        return createJobApplicationResponse(jobApplication);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JobApplicationResponse updateJobApplication(UpdateJobApplicationRequest request) throws NotFoundException, BadRequestException {
        validationUtil.validate(request);

        Job job = jobService.findById(request.getJobId());

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!userId.equals(job.getStudent().getId())) {
            throw new AccessDeniedException(StatusMessages.ACCESS_DENIED);
        }

        JobApplication jobApplication = jobApplicationRepository.findByJobIdAndTutorId(job.getId(), request.getTutorId())
                .orElseThrow(() -> new NotFoundException(StatusMessages.NOT_FOUND));

        Status statusToUpdate = Status.valueOf(request.getStatus());

        if (jobApplication.getStatus() != Status.ACTIVE) {
            throw new BadRequestException("The job application status cannot be updated further.");
        }

        jobApplication.setStatus(statusToUpdate);

        jobApplicationRepository.saveAndFlush(jobApplication);

        return createJobApplicationResponse(jobApplication);
    }

    private JobApplicationResponse createJobApplicationResponse(JobApplication jobApplication) {
        return JobApplicationResponse.builder()
                .jobId(jobApplication.getJob().getId())
                .tutorId(jobApplication.getTutor().getId())
                .status(String.valueOf(jobApplication.getStatus()))
                .build();
    }

    private ListJobApplicationResponse createListJobApplicationResponse(JobApplication jobApplication) {
        Job job = jobApplication.getJob();
        return ListJobApplicationResponse.builder()
                .jobId(job.getId())
                .title(job.getTitle())
                .subject(job.getSubject())
                .status(String.valueOf(jobApplication.getStatus()))
                .deadline(String.valueOf(job.getDeadline()))
                .build();
    }
}