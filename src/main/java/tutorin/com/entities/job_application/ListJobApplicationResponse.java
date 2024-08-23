package tutorin.com.entities.job_application;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListJobApplicationResponse {
    private String jobId;
    private String title;
    private String subject;
    private String status;
    private String deadline;
}

