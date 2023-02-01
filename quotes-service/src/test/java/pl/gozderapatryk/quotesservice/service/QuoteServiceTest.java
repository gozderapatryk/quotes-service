package pl.gozderapatryk.quotesservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.gozderapatryk.quotesservice.dto.request.CreateQuoteRequestDto;
import pl.gozderapatryk.quotesservice.dto.response.GetAuthorDto;
import pl.gozderapatryk.quotesservice.dto.response.GetQuoteDto;
import pl.gozderapatryk.quotesservice.entity.Author;
import pl.gozderapatryk.quotesservice.entity.Quote;
import pl.gozderapatryk.quotesservice.exception.AuthorNotFoundException;
import pl.gozderapatryk.quotesservice.exception.QuoteAlreadyExistException;
import pl.gozderapatryk.quotesservice.exception.QuoteNotFoundException;
import pl.gozderapatryk.quotesservice.repository.AuthorRepository;
import pl.gozderapatryk.quotesservice.repository.QuoteRepository;

import javax.validation.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
public class QuoteServiceTest {

    @TestConfiguration
    @ImportAutoConfiguration(ValidationAutoConfiguration.class)
    public static class AuthorServiceTestConfiguration {

        @MockBean
        public QuoteRepository quoteRepository;

        @MockBean
        public AuthorRepository authorRepository;

        @Bean
        public QuoteService quoteService() {
            return new QuoteService(quoteRepository, authorRepository);
        }
    }

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private QuoteService quoteService;

    private List<Quote> quotes = new ArrayList<>();

    @BeforeEach
    public void setup() {
        var dalaiLama = new Author(1L, "Dalai", null, "Lama", new HashSet<>());
        var nelsonMandela = new Author(2L, "Nelson", null, "Mandela", new HashSet<>());
        var steveJobs = new Author(3L, "Steve", null, "Jobs", new HashSet<>());

        quotes = List.of(
                new Quote(1L, "The purpose of our lives is to be happy.", dalaiLama),
                new Quote(2L, "If you really look closely, most overnight successes took a long time.", steveJobs),
                new Quote(3L, "Your time is limited, so don't waste it living someone else's life. Don't be trapped by dogma – which is living with the results of other people's thinking.", steveJobs),
                new Quote(4L, "The greatest glory in living lies not in never falling, but in rising every time we fall.", nelsonMandela),
                new Quote(5L, "The way to get started is to quit talking and begin doing.", steveJobs)
        );
    }

    @Test
    public void testFindQuoteByIdWhenSuccess() {
        var quote = quotes.get(0);

        Mockito
                .when(quoteRepository.findById(anyLong()))
                .thenReturn(Optional.of(quote));

        var result = quoteService.findQuoteById(quote.getId());
        var expected = GetQuoteDto.builder()
                .id(quote.getId())
                .quote(quote.getQuote())
                .author(GetAuthorDto.builder()
                        .id(quote.getAuthor().getId())
                        .firstName(quote.getAuthor().getFirstName())
                        .middleName(quote.getAuthor().getMiddleName())
                        .lastName(quote.getAuthor().getLastName())
                        .build())
                .build();

        assertEquals(expected, result);
    }

    @Test
    public void testFindQuoteByIdWhenDoesNotExist() {
        var quote = quotes.get(0);
        var expectedErrorMessage = String.format("Quote with the following id: %s does not exist.", quote.getId());

        Mockito
                .when(quoteRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        QuoteNotFoundException thrown = assertThrows(QuoteNotFoundException.class, () -> {
            quoteService.findQuoteById(quote.getId());
        });

        assertEquals(expectedErrorMessage, thrown.getMessage());
    }

    @Test
    public void testAddQuoteWhenQuoteIsNull() {
        var createQuoteRequestDto = new CreateQuoteRequestDto(null, 1L);

        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> {
            quoteService.addQuote(createQuoteRequestDto);
        });
    }

    @Test
    public void testAddQuoteWhenAuthorIdIsNull() {
        var createQuoteRequestDto = new CreateQuoteRequestDto("Example quote to add", null);

        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> {
            quoteService.addQuote(createQuoteRequestDto);
        });
    }

    @Test
    public void testAddQuoteWhenNull() {
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> {
            quoteService.addQuote(null);
        });
    }

    @Test
    public void testAddQuoteWhenQuoteAlreadyExist() {
        var createQuoteRequestDto = new CreateQuoteRequestDto("Example quote to add", 1L);
        var expectedErrorMessage = "Quote already exists.";

        Mockito
                .when(quoteRepository.existsByQuote(anyString()))
                .thenReturn(true);

        QuoteAlreadyExistException thrown = assertThrows(QuoteAlreadyExistException.class, () -> {
            quoteService.addQuote(createQuoteRequestDto);
        });

        assertEquals(expectedErrorMessage, thrown.getMessage());
    }

    @Test
    public void testAddQuoteWhenAuthorDoesNotExist() {
        var authorId = 1L;
        var createQuoteRequestDto = new CreateQuoteRequestDto("Example quote to add", authorId);
        var expectedErrorMessage = String.format("Author with the following id: %s does not exist.", authorId);

        Mockito
                .when(authorRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        AuthorNotFoundException thrown = assertThrows(AuthorNotFoundException.class, () -> {
            quoteService.addQuote(createQuoteRequestDto);
        });

        assertEquals(expectedErrorMessage, thrown.getMessage());
    }

    @Test
    public void testAddQuoteWhenOk() {
        var author = quotes.get(0).getAuthor();
        var createQuoteRequestDto = new CreateQuoteRequestDto("Example quote to add", author.getId());
        var savedQuote = new Quote(1L, "Example quote to add", author);

        Mockito
                .when(quoteRepository.existsByQuote(anyString()))
                .thenReturn(false);

        Mockito
                .when(authorRepository.findById(anyLong()))
                .thenReturn(Optional.of(author));

        Mockito
                .when(quoteRepository.save(any(Quote.class)))
                .thenReturn(savedQuote);

        quoteService.addQuote(createQuoteRequestDto);

        Mockito.verify(quoteRepository, Mockito.times(1)).existsByQuote(anyString());
        Mockito.verify(authorRepository, Mockito.times(1)).findById(anyLong());
        Mockito.verify(quoteRepository, Mockito.times(1)).save(any(Quote.class));
    }

    @Test
    public void testDeleteQuoteByIdWhenDoesNotExist() {
        var quoteId = 1L;
        var expectedErrorMessage = String.format("Quote with the following id: %s does not exist.", quoteId);

        Mockito
                .when(authorRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        QuoteNotFoundException thrown = assertThrows(QuoteNotFoundException.class, () -> {
            quoteService.deleteQuoteById(quoteId);
        });

        assertEquals(expectedErrorMessage, thrown.getMessage());
    }
}
