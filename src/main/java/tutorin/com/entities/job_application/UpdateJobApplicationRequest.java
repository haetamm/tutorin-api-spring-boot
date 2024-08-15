package tutorin.com.entities.job_application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tutorin.com.annotation.job_application.ValidStatus;
import tutorin.com.constant.Status;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateJobApplicationRequest {
    @NotBlank(message = "Job id cannot be blank")
    private String jobId;

    @NotBlank(message = "Tutor id cannot be blank")
    private String tutorId;

    @NotNull(message = "Status cannot be null")
    @ValidStatus(enumClass = Status.class, message = "Invalid status value")
    private String status;
}
