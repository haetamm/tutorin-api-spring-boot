package tutorin.com.entities.profile;

import lombok.*;
import tutorin.com.entities.file.FileResponse;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {
    private String name;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String country;
    private String postcode;
    private FileResponse image;
    private FileResponse resume;
}
