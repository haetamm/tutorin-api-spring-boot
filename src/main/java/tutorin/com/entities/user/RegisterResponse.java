package tutorin.com.entities.user;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    private String name;
    private String username;
    private List<String> roles;
}
