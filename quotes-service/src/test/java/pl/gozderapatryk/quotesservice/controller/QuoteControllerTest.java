package pl.gozderapatryk.quotesservice.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.gozderapatryk.quotesservice.dto.response.GetAuthorDto;
import pl.gozderapatryk.quotesservice.dto.response.GetQuoteDto;
import pl.gozderapatryk.quotesservice.exception.QuoteNotFoundException;
import pl.gozderapatryk.quotesservice.service.QuoteService;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;

@ExtendWith(SpringExtension.class)
@WebMvcTest(QuoteController.class)
public class QuoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuoteService quoteService;

    @Test
    public void testFindQuoteByIdWhenSuccess() throws Exception {
        var quoteId = 1L;
        var getQuoteDto = GetQuoteDto.builder()
                .id(quoteId)
                .quote("Your time is limited, so don't waste it living someone else's life. Don't be trapped by dogma – which is living with the results of other people's thinking.")
                .author(GetAuthorDto.builder()
                        .firstName("Steve")
                        .lastName("Jobs")
                        .build())
                .build();

        Mockito
                .when(quoteService.findQuoteById(quoteId))
                .thenReturn(getQuoteDto);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/quotes/{id}", quoteId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(getQuoteDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quote", Matchers.is(getQuoteDto.getQuote())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author.firstName", Matchers.is(getQuoteDto.getAuthor().getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author.lastName", Matchers.is(getQuoteDto.getAuthor().getLastName())));
    }

    @Test
    public void testFindQuoteByIdWhenNotFound() throws Exception {
        var quoteId = 1L;
        var errorMessage = String.format("Quote with the following id: %s does not exist.", quoteId);

        Mockito
                .when(quoteService.findQuoteById(quoteId))
                .thenThrow(new QuoteNotFoundException(quoteId));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/quotes/{id}", quoteId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(errorMessage)));
    }

    @Test
    public void testFindQuoteByIdWhenIdIsNotPositive() throws Exception {
        var quoteId = 0L;

        Mockito
                .when(quoteService.findQuoteById(quoteId))
                .thenThrow(new ConstraintViolationException("Validation problems - check details.", new HashSet<>()));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/quotes/{id}", quoteId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Validation problems - check details.")));
    }
}
