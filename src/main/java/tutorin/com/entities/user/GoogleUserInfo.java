package tutorin.com.entities.user;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoogleUserInfo {
    private String id;
    private String email;
    private String username;
    private String name;
}
