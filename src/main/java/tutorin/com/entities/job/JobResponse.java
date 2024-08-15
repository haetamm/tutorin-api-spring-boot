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
    private String studentId;
    private String title;
    private String subject;
    private String gender;
    private String education;
    private Date deadline;
    private String address;
    private String city;
    private String country;
    private String salary;
    private String description;
    private String createdAt;
    private String updatedAt;
}
