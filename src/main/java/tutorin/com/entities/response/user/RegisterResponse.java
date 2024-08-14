package tutorin.com.entities.response.user;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    private String username;
    private List<String> roles;
}
