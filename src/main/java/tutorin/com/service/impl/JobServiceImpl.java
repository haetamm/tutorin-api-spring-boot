package tutorin.com.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorin.com.constant.Gender;
import tutorin.com.constant.StatusMessages;
import tutorin.com.entities.job.JobRequest;
import tutorin.com.entities.job.JobResponse;
import tutorin.com.exception.NotFoundException;
import tutorin.com.model.Job;
import tutorin.com.model.User;
import tutorin.com.repository.JobRepository;
import tutorin.com.service.JobService;
import tutorin.com.service.UserService;
import tutorin.com.validation.ValidationUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final ValidationUtil validationUtil;
    private final JobRepository jobRepository;
    private final UserService userService;

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

        return createJobResponse(job);
    }

    @Transactional(readOnly = true)
    @Override
    public JobResponse getJobById(String id) throws NotFoundException {
        Job job = findById(id);
        return createJobResponse(job);
    }

    @Transactional(readOnly = true)
    @Override
    public List<JobResponse> getAllJob() {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream()
                .map(this::createJobResponse)
                .collect(Collectors.toList());
    }

    private Job findById(String id) throws NotFoundException {
        return jobRepository.findById(id).orElseThrow(() -> new NotFoundException(StatusMessages.NOT_FOUND));
    }

    private JobResponse createJobResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .studentId(job.getStudent().getId())
                .title(job.getTitle())
                .subject(job.getSubject())
                .gender(String.valueOf(job.getGender()))
                .education(job.getEducation())
                .deadline(job.getDeadline())
                .address(job.getAddress())
                .city(job.getCity())
                .country(job.getCountry())
                .salary(job.getSalary())
                .description(job.getDescription())
                .createdAt(String.valueOf(job.getCreatedAt()))
                .updatedAt(String.valueOf(job.getUpdatedAt()))
                .build();
    }
}
