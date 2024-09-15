package tutorin.com.entities.user;

import jakarta.validation.constraints.*;
import lombok.*;
import tutorin.com.annotation.job.ValidEnum;
import tutorin.com.annotation.user.UniqueEmail;
import tutorin.com.annotation.user.UniqueUsername;
import tutorin.com.constant.UserRoleRegEnum;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterWithGoogleRequest {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "must contain only alphabet characters and spaces")
    @Size(min = 4, max = 23)
    private String name;

    @NotBlank
    @UniqueUsername
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "must contain only alphanumeric characters")
    @Size(min = 3, max = 8)
    private String username;

    @NotBlank
    @UniqueEmail
    @Email(message = "must be a valid email address")
    private String email;

    private String password = "";

    @NotNull(message = "Role cannot be null")
    @ValidEnum(enumClass = UserRoleRegEnum.class, message = "Invalid role value")
    private String role;
}
