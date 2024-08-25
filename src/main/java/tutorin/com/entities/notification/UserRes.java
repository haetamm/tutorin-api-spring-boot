package tutorin.com.entities.notification;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRes {
    private String id;
    private String name;
    private String status;
    private String createdAt;
    private String updatedAt;
}
