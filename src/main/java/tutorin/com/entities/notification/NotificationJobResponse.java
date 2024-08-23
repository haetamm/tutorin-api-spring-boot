package tutorin.com.entities.notification;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationJobResponse {
    private String jobId;
    private String tutorId;
    private String title;
    private String subject;
    private String deadline;
    private List<UserRes> tutors;
}
