package pl.gozderapatryk.quotesservice.mapper;

import pl.gozderapatryk.quotesservice.dto.request.CreateAuthorRequestDto;
import pl.gozderapatryk.quotesservice.dto.response.GetAuthorDto;
import pl.gozderapatryk.quotesservice.dto.response.GetQuoteDto;
import pl.gozderapatryk.quotesservice.entity.Author;
import pl.gozderapatryk.quotesservice.entity.Quote;

import java.util.HashSet;
import java.util.Objects;

public interface ModelMapper {

    static Author toAuthorEntity(CreateAuthorRequestDto createAuthorRequestDto) {
        return new Author(null, createAuthorRequestDto.getFirstName(), createAuthorRequestDto.getMiddleName(), createAuthorRequestDto.getLastName(), new HashSet<>());
    }

    static GetQuoteDto toGetQuoteDto(Quote quote) {
        return Objects.isNull(quote) ? null : GetQuoteDto.builder()
                .id(quote.getId())
                .quote(quote.getQuote())
                .author(toGetAuthorDto(quote.getAuthor()))
                .build();
    }

    static GetAuthorDto toGetAuthorDto(Author savedAuthor) {
        return Objects.isNull(savedAuthor) ? null : GetAuthorDto.builder()
                .id(savedAuthor.getId())
                .firstName(savedAuthor.getFirstName())
                .middleName(savedAuthor.getMiddleName())
                .lastName(savedAuthor.getLastName())
                .build();
    }
}
