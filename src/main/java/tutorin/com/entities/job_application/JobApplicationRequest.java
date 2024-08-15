package tutorin.com.entities.job_application;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobApplicationRequest {
    @NotBlank(message = "Job id cannot be blank")
    private String jobId;
}
