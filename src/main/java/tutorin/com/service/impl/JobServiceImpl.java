package tutorin.com.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorin.com.constant.Gender;
import tutorin.com.entities.job.JobRequest;
import tutorin.com.entities.job.JobResponse;
import tutorin.com.exception.NotFoundException;
import tutorin.com.model.Job;
import tutorin.com.model.User;
import tutorin.com.repository.JobApplicationRepository;
import tutorin.com.repository.JobRepository;
import tutorin.com.service.JobService;
import tutorin.com.service.UserService;
import tutorin.com.validation.ValidationUtil;

import static tutorin.com.helper.Utilities.createJobResponse;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final ValidationUtil validationUtil;
    private final JobRepository jobRepository;
    private final UserService userService;
    private final JobApplicationRepository jobApplicationRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JobResponse createJob(JobRequest request) throws NotFoundException {
        validationUtil.validate(request);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUserId(userId);

        Job job = jobRepository.saveAndFlush(Job.builder()
                .student(user)
                .title(request.getTitle())
                .subject(request.getSubject())
                .gender(Gender.valueOf(request.getGender()))
                .education(request.getEducation())
                .deadline(request.getDeadline())
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .salary(request.getSalary())
                .description(request.getDescription())
                .build());

        return createJobResponse(job, jobApplicationRepository);
    }

    @Transactional(readOnly = true)
    @Override
    public JobResponse getJobById(String id) throws NotFoundException {
        Job job = findById(id);
        return createJobResponse(job, jobApplicationRepository);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<JobResponse> getAllJob(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Job> jobPage = jobRepository.findAll(pageable);

        return jobPage.map(job -> createJobResponse(job, jobApplicationRepository));
    }

    @Transactional(readOnly = true)
    @Override
    public Job findById(String id) throws NotFoundException {
        return jobRepository.findById(id).orElseThrow(() -> new NotFoundException("Job not found"));
    }


}
