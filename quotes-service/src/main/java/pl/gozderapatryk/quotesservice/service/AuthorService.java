package pl.gozderapatryk.quotesservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.gozderapatryk.quotesservice.dto.request.CreateAuthorRequestDto;
import pl.gozderapatryk.quotesservice.dto.response.GetAuthorDto;
import pl.gozderapatryk.quotesservice.dto.response.FindAuthorsResponseDto;
import pl.gozderapatryk.quotesservice.entity.Quote;
import pl.gozderapatryk.quotesservice.exception.AuthorAlreadyExistException;
import pl.gozderapatryk.quotesservice.exception.AuthorNotFoundException;
import pl.gozderapatryk.quotesservice.exception.QuoteNotFoundException;
import pl.gozderapatryk.quotesservice.mapper.ModelMapper;
import pl.gozderapatryk.quotesservice.repository.AuthorRepository;
import pl.gozderapatryk.quotesservice.repository.QuoteRepository;
import pl.gozderapatryk.quotesservice.validation.EnumValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class AuthorService {

    private final AuthorRepository authorRepository;

    public FindAuthorsResponseDto findAuthors(@Min(value = 0) int page, @Min(value = 1) @Max(value = 50) int limit, @EnumValue(enumClass = Sort.Direction.class) String sort, @Size(min = 3) String author) {
        var pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.valueOf(sort.toUpperCase()), "lastName", "firstName"));

        var authorsPage = authorRepository.findAll(pageable, author);

        return FindAuthorsResponseDto.builder()
                .currentPage(authorsPage.getNumber())
                .limit(authorsPage.getNumberOfElements())
                .totalItems(authorsPage.getTotalElements())
                .totalPages(authorsPage.getTotalPages())
                .authors(authorsPage.getContent().stream().map(ModelMapper::toGetAuthorDto).collect(Collectors.toList()))
                .build();
    }

    public GetAuthorDto createAuthor(@NotNull @Valid CreateAuthorRequestDto createAuthorRequestDto) {

        if (authorRepository.existsByFirstNameAndLastName(createAuthorRequestDto.getFirstName(), createAuthorRequestDto.getLastName())) {
            throw new AuthorAlreadyExistException();
        }

        var author = ModelMapper.toAuthorEntity(createAuthorRequestDto);
        var savedAuthor = authorRepository.save(author);
        return ModelMapper.toGetAuthorDto(savedAuthor);
    }

    public void deleteQuotesByAuthorId(@NotNull @Positive Long id) {
        authorRepository.findById(id)
                .ifPresentOrElse(author -> {
                    for (Quote quote: new HashSet<>(author.getQuotes())) {
                        author.removeQuote(quote);
                    }
                    authorRepository.save(author);
                }, () -> {
                    throw new AuthorNotFoundException(id);
                });
    }
}
