package tutorin.com.entities;


import lombok.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebErrorResponse<T> {
    private int code;
    private String status;
    private T messages;
}
