package pl.gozderapatryk.quotesservice.dto.response;

import lombok.*;
import pl.gozderapatryk.quotesservice.dto.PaginationDto;

import java.util.List;

@Getter
@ToString
public class FindAuthorsResponseDto extends PaginationDto {
    private List<GetAuthorDto> authors;

    @Builder
    public FindAuthorsResponseDto(Integer currentPage, Integer limit, Long totalItems, Integer totalPages, List<GetAuthorDto> authors) {
        super(currentPage, limit, totalItems, totalPages);
        this.authors = authors;
    }
}
