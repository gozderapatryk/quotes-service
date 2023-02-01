package pl.gozderapatryk.quotesservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorResponseDto {
    private String message;
    private String path;
    private Map<String, Set<String>> details;
    private LocalDateTime createdAt;

    @Builder
    public ErrorResponseDto(String message, String path, Map<String, Set<String>> details) {
        this.message = message;
        this.path = path;
        this.details = details;
        this.createdAt = LocalDateTime.now();
    }
}
