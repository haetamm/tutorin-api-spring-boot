package tutorin.com.entities.user;

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
public class ResetPasswordRequest {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "must contain only alphanumeric characters")
    @Size(min = 4, max = 8)
    private String password;
}
