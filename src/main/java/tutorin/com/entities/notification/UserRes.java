package tutorin.com.entities.notification;

import lombok.*;
import tutorin.com.entities.image.ImageResponse;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRes {
    private String id;
    private String name;
    private ImageResponse image;
    private String status;
    private String createdAt;
    private String updatedAt;
}
