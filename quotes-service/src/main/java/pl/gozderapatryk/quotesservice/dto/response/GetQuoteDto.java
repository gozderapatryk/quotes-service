package pl.gozderapatryk.quotesservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetQuoteDto {
    private Long id;
    private String quote;
    private GetAuthorDto author;
}
