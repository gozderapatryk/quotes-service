package pl.gozderapatryk.quotesservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.gozderapatryk.quotesservice.dto.request.CreateAuthorRequestDto;
import pl.gozderapatryk.quotesservice.entity.Author;
import pl.gozderapatryk.quotesservice.entity.Quote;
import pl.gozderapatryk.quotesservice.exception.AuthorAlreadyExistException;
import pl.gozderapatryk.quotesservice.exception.AuthorNotFoundException;
import pl.gozderapatryk.quotesservice.repository.AuthorRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
public class AuthorServiceTest {

    @TestConfiguration
    public static class AuthorServiceTestConfiguration {

        @MockBean
        public AuthorRepository authorRepository;

        @Bean
        public AuthorService authorService() {
            return new AuthorService(authorRepository);
        }
    }

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorService authorService;

    private List<Author> authors = new ArrayList<>();

    @BeforeEach
    public void setup() {
        authors = List.of(
                new Author(1L, "John", null, "Joe", new HashSet<>()),
                new Author(2L, "Johnny", null, "Doe", new HashSet<>()),
                new Author(3L, "Joe", null, "Johnson", new HashSet<>()),
                new Author(4L, "Walt", null, "Disney", new HashSet<>()),
                new Author(5L, "Steve", null, "Jobs", new HashSet<>())
        );
    }

    @Test
    public void testFindAuthorsWhenSuccess() {
        var page = 0;
        var limit = 2;
        var pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "lastName", "firstName"));
        var author = "John";

        var mockedAuthors = authors.stream()
                .filter(a -> (Objects.nonNull(a.getFirstName()) && a.getFirstName().contains(author)) || (Objects.nonNull(a.getMiddleName()) && a.getMiddleName().contains(author)) || (Objects.nonNull(a.getLastName()) && a.getLastName().contains(author)))
                .sorted(Comparator.comparing(Author::getLastName).thenComparing(Author::getFirstName))
                .collect(Collectors.toList());

        var pageAuthors = new PageImpl<>(mockedAuthors.subList(0, 2), pageable, mockedAuthors.size());

        Mockito
                .when(authorRepository.findAll(pageable, author))
                .thenReturn(pageAuthors);

        var result = authorService.findAuthors(page, limit, "ASC", author);

        assertEquals(0, result.getCurrentPage().intValue());
        assertEquals(2, result.getLimit().intValue());
        assertEquals(3, result.getTotalItems().longValue());
        assertEquals(2, result.getTotalPages().intValue());
        assertEquals(2, result.getAuthors().size());
    }

    @Test
    public void testFindAuthorsWhenNoAuthorFound() {
        var page = 0;
        var limit = 2;
        var pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "lastName", "firstName"));
        var author = "John";
        var pageAuthors = new PageImpl<>(new ArrayList<Author>(), pageable, 0);

        Mockito
                .when(authorRepository.findAll(pageable, author))
                .thenReturn(pageAuthors);

        var result = authorService.findAuthors(page, limit, "ASC", author);

        assertEquals(0, result.getCurrentPage().intValue());
        assertEquals(0, result.getLimit().intValue());
        assertEquals(0, result.getTotalItems().longValue());
        assertEquals(0, result.getTotalPages().intValue());
        assertEquals(0, result.getAuthors().size());
    }

    @Test
    public void testCreateAuthorWhenAlreadyExist() {
        Mockito
                .when(authorRepository.existsByFirstNameAndLastName(anyString(), anyString()))
                .thenReturn(true);

        AuthorAlreadyExistException thrown = assertThrows(AuthorAlreadyExistException.class, () -> {
            var createAuthorDto = new CreateAuthorRequestDto("Steve", null, "Jobs");
            authorService.createAuthor(createAuthorDto);
        });

        assertEquals("Author already exists.", thrown.getMessage());
    }

    @Test
    public void testDeleteQuotesByAuthorIdWhenSuccess() {

        var author = authors.get(0);
        Set<Quote> quotes = Stream.of(
                new Quote(1L, "Sometimes life hits you in the head with a brick. Don't lose faith.", author),
                new Quote(2L, "Design is not just what it looks like and feels like. Design is how it works.", author)
        ).collect(Collectors.toSet());
        author.setQuotes(quotes);

        Mockito
                .when(authorRepository.findById(author.getId()))
                .thenReturn(Optional.of(author));

        Mockito
                .when(authorRepository.save(author))
                .thenReturn(author);

        authorService.deleteQuotesByAuthorId(author.getId());

        Mockito.verify(authorRepository, Mockito.times(1)).findById(author.getId());
        Mockito.verify(authorRepository, Mockito.times(1)).save(author);
    }
    @Test
    public void testDeleteQuotesByAuthorIdWhenDoesNotExist() {
        Mockito
                .when(authorRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        var authorId = 1L;
        var expectedErrorMessage = String.format("Author with the following id: %s does not exist.", authorId);

        AuthorNotFoundException thrown = assertThrows(AuthorNotFoundException.class, () -> {
            authorService.deleteQuotesByAuthorId(1L);
        });

        assertEquals(expectedErrorMessage, thrown.getMessage());
    }
}