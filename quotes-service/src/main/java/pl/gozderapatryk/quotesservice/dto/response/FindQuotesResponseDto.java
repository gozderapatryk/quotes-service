package pl.gozderapatryk.quotesservice.dto.response;

import lombok.*;
import pl.gozderapatryk.quotesservice.dto.PaginationDto;

import java.util.List;

@Getter
@ToString
public class FindQuotesResponseDto extends PaginationDto {
    private List<GetQuoteDto> quotes;

    @Builder
    public FindQuotesResponseDto(Integer currentPage, Integer limit, Long totalItems, Integer totalPages, List<GetQuoteDto> quotes) {
        super(currentPage, limit, totalItems, totalPages);
        this.quotes = quotes;
    }
}
