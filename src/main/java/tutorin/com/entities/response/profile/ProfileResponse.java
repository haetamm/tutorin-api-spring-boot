package tutorin.com.entities.response.profile;

import lombok.*;

import java.util.List;

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
}
