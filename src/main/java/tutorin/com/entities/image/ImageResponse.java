package tutorin.com.entities.image;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponse {
    private String id;
    private String name;
    private String url;
}
