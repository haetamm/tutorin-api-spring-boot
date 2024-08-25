package tutorin.com.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tutorin.com.entities.notification.NotificationJobResponse;
import tutorin.com.entities.notification.UserRes;
import tutorin.com.exception.NotFoundException;
import tutorin.com.model.Job;
import tutorin.com.model.JobApplication;
import tutorin.com.repository.JobApplicationRepository;
import tutorin.com.repository.JobRepository;
import tutorin.com.service.JobService;
import tutorin.com.service.NotificationService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobService jobService;

    @Override
    public List<NotificationJobResponse> getNotificationJob() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Job> jobs = jobRepository.findAllByStudentId(userId);
        return jobs.stream()
                .map(this::createNotificationJobResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationJobResponse getNotificationJobById(String id) throws NotFoundException {
        Job job = jobService.findById(id);
        return createNotificationJobResponse(job);
    }

    private NotificationJobResponse createNotificationJobResponse(Job job) {
        List<UserRes> tutors = jobApplicationRepository.findAllByJobId(job.getId())
                .stream()
                .sorted(Comparator.comparing(JobApplication::getCreatedAt))
                .map(this::mapToUserRes)
                .collect(Collectors.toList());

        return NotificationJobResponse.builder()
                .jobId(job.getId())
                .tutorId(tutors.isEmpty() ? null : tutors.getFirst().getId())
                .title(job.getTitle())
                .subject(job.getSubject())
                .deadline(String.valueOf(job.getDeadline()))
                .tutors(tutors)
                .build();
    }

    private UserRes mapToUserRes(JobApplication jobApplication) {
        return UserRes.builder()
                .id(jobApplication.getTutor().getId())
                .name(jobApplication.getTutor().getName())
                .status(String.valueOf(jobApplication.getStatus()))
                .createdAt(String.valueOf(jobApplication.getCreatedAt()))
                .updatedAt(String.valueOf(jobApplication.getUpdatedAt()))
                .build();
    }
}

