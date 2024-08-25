package tutorin.com.entities.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForgotPasswordRequest {
    @NotBlank(message = "Email is required")
    @Email
    private String email;
}
