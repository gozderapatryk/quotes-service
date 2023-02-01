package pl.gozderapatryk.quotesservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.gozderapatryk.quotesservice.dto.request.CreateQuoteRequestDto;
import pl.gozderapatryk.quotesservice.dto.response.GetQuoteDto;
import pl.gozderapatryk.quotesservice.dto.response.FindQuotesResponseDto;
import pl.gozderapatryk.quotesservice.dto.request.UpdateQuoteRequestDto;
import pl.gozderapatryk.quotesservice.entity.Quote;
import pl.gozderapatryk.quotesservice.exception.AuthorNotFoundException;
import pl.gozderapatryk.quotesservice.exception.QuoteAlreadyExistException;
import pl.gozderapatryk.quotesservice.exception.QuoteNotFoundException;
import pl.gozderapatryk.quotesservice.mapper.ModelMapper;
import pl.gozderapatryk.quotesservice.repository.AuthorRepository;
import pl.gozderapatryk.quotesservice.repository.QuoteRepository;
import pl.gozderapatryk.quotesservice.validation.EnumValue;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class QuoteService {

    private final QuoteRepository quoteRepository;
    private final AuthorRepository authorRepository;

    public FindQuotesResponseDto findQuotes(@Min(value = 0) int page, @Min(value = 1) @Max(value = 50) int limit, @EnumValue(enumClass = Sort.Direction.class) String sort, @Positive Long authorId) {
        var pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.valueOf(sort.toUpperCase()), "createdAt"));

        var quotesPage = quoteRepository.findAll(pageable, authorId);

        return FindQuotesResponseDto.builder()
                .currentPage(quotesPage.getNumber())
                .limit(quotesPage.getNumberOfElements())
                .totalItems(quotesPage.getTotalElements())
                .totalPages(quotesPage.getTotalPages())
                .quotes(quotesPage.getContent().stream().map(ModelMapper::toGetQuoteDto).collect(Collectors.toList()))
                .build();
    }

    public void addQuote(@NotNull @Valid CreateQuoteRequestDto createQuoteRequestDto) {

        if(quoteRepository.existsByQuote(createQuoteRequestDto.getQuote())) {
            throw new QuoteAlreadyExistException();
        }

        var author = authorRepository.findById(createQuoteRequestDto.getAuthorId())
                        .orElseThrow(() -> new AuthorNotFoundException(createQuoteRequestDto.getAuthorId()));

        var quoteToSave = new Quote(null, createQuoteRequestDto.getQuote(), author);
        quoteRepository.save(quoteToSave);
    }

    public void deleteQuoteById(@NotNull @Positive Long id) {
        quoteRepository.findById(id)
                .ifPresentOrElse(quoteRepository::delete, () -> {
                    throw new QuoteNotFoundException(id);
                });
    }

    public GetQuoteDto findQuoteById(@NotNull @Positive Long id) {
        return quoteRepository.findById(id)
                .map(ModelMapper::toGetQuoteDto)
                .orElseThrow(() -> new QuoteNotFoundException(id));
    }

    public void updateQuote(@NotNull @Positive Long id, @NotNull @Valid UpdateQuoteRequestDto updateQuoteRequestDto) {
        quoteRepository.findById(id)
                .ifPresentOrElse(quote -> {
                    quote.setQuote(updateQuoteRequestDto.getQuote());
                    quoteRepository.save(quote);
                }, () -> {
                    throw new QuoteNotFoundException(id);
                });
    }
}