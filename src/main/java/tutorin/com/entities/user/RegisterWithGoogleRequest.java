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
    @UniqueUsername
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "must contain only alphanumeric characters")
    @Size(min = 3, max = 8)
    private String username;

    @NotBlank
    private String tokenAccess;

    @NotNull(message = "Role cannot be null")
    @ValidEnum(enumClass = UserRoleRegEnum.class, message = "Invalid role value")
    private String role;
}
