package tutorin.com.entities.job;

import jakarta.validation.constraints.*;
import lombok.*;
import tutorin.com.annotation.job.FutureDate;
import tutorin.com.annotation.job.ValidEnum;
import tutorin.com.constant.Gender;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequest {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Subject cannot be blank")
    @Size(max = 20, message = "Subject must not exceed 100 characters")
    private String subject;

    @NotNull(message = "Gender cannot be null")
    @ValidEnum(enumClass = Gender.class, message = "Invalid gender value")
    private String gender;

    @NotBlank(message = "Education cannot be blank")
    @Size(max = 50, message = "Education must not exceed 50 characters")
    private String education;

    @NotNull(message = "Deadline cannot be null")
    @FutureDate(message = "Deadline must be a future date")
    private Date deadline;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 50, message = "City must not exceed 100 characters")
    private String city;

    @NotBlank(message = "Country cannot be blank")
    @Size(max = 50, message = "Country must not exceed 100 characters")
    private String country;

    @NotBlank(message = "Salary cannot be blank")
    @Size(max = 255, message = "Salary must not exceed 255 characters")
    private String salary;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 6000, message = "Description must not exceed 10,000 characters")
    private String description;
}

