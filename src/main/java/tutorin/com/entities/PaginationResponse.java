package tutorin.com.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse {

    private Integer totalPages;

    private Long totalElement;

    private Integer page;

    private Integer size;

    private Boolean hasNext;

    private Boolean hasPrevious;

}

