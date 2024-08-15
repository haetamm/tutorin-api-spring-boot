package tutorin.com.entities.request.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileRequest {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "must contain only alphabet characters and spaces")
    @Size(min = 4, max = 23)
    private String name;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "must contain only alphanumeric characters")
    @Size(min = 3, max = 8)
    private String username;

    @NotBlank
    @Email(message = "must be a valid email address")
    private String email;


    @Pattern(regexp = "^[0-9]+$", message = "Phone number must contain only digits")
    @Size(min = 8, max = 15, message = "Phone number must be between 8 and 15 digits")
    private String phone;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @NotBlank(message = "Country cannot be blank")
    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    @Pattern(regexp = "^[0-9]+$", message = "Postcode must contain only digits")
    @Size(max = 20, message = "Postcode must not exceed 20 characters")
    private String postcode;
}
