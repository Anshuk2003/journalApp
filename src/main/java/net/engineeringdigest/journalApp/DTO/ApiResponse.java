package net.engineeringdigest.journalApp.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ApiResponse<T> {
    private String title;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private T data;

}
