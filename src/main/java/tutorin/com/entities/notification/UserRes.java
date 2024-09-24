package tutorin.com.entities.notification;

import lombok.*;
import tutorin.com.entities.file.FileResponse;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRes {
    private String id;
    private String name;
    private FileResponse image;
    private FileResponse resume;
    private String status;
    private String createdAt;
    private String updatedAt;
}
