package tutorin.com.entities.user;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoogleTokenResponse {
    private String access_token;
    private String token_type;
    private Long expires_in;
    private String refresh_token;
}
