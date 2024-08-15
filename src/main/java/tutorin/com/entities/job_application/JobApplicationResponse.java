package tutorin.com.entities.job_application;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobApplicationResponse {
    private String jobId;
    private String tutorId;
    private String status;
}
