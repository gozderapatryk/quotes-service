package pl.gozderapatryk.quotesservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class PaginationDto {
    private Integer currentPage;
    private Integer limit;
    private Long totalItems;
    private Integer totalPages;
}
