package tutorin.com.entities.job;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobResponse {
    private String id;
    private Boolean applied;
    private String title;
    private String subject;
    private String gender;
    private String education;
    private String deadline;
    private String address;
    private String city;
    private String country;
    private String salary;
    private String description;
    private String createdAt;
    private String updatedAt;
}
