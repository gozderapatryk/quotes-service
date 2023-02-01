package pl.gozderapatryk.quotesservice.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.gozderapatryk.quotesservice.dto.request.CreateQuoteRequestDto;
import pl.gozderapatryk.quotesservice.dto.response.GetQuoteDto;
import pl.gozderapatryk.quotesservice.dto.request.UpdateQuoteRequestDto;
import pl.gozderapatryk.quotesservice.dto.response.FindQuotesResponseDto;
import pl.gozderapatryk.quotesservice.service.QuoteService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quotes")
public class QuoteController {

    private final QuoteService quoteService;


    @GetMapping
    @ApiOperation(value = "Retrieves a list of quotes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieved a list of quotes")
    })
    public FindQuotesResponseDto findQuotes(
            @ApiParam(name = "page", value = "Results page you want to retrieve (0...N)", type = "integer", required = true, defaultValue = "1") @RequestParam(defaultValue = "0") int page,
            @ApiParam(name = "limit", value = "Number of records per page ", type = "integer",  required = true, defaultValue = "20") @RequestParam(defaultValue = "20") int limit,
            @ApiParam(name = "sort", value = "Sorting direction of authors (asc or desc)", type = "string", required = true, defaultValue = "asc") @RequestParam(defaultValue = "asc") String sort,
            @ApiParam(name = "author", value = "Id of the author", type = "long") @RequestParam(required = false) Long authorId
    ) {
       return quoteService.findQuotes(page, limit, sort, authorId);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Retrieves a quote by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieved a quote by id"),
            @ApiResponse(code = 404, message = "Quote does not exist")
    })
    public GetQuoteDto findQuoteById(@ApiParam(name = "id", value = "Id of the quote", type = "long", required = true) @PathVariable Long id) {
        return quoteService.findQuoteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Creates a new quote")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Quote created successfully"),
            @ApiResponse(code = 404, message = "Author of the quote does not exist"),
            @ApiResponse(code = 409, message = "Quote already exists"),
            @ApiResponse(code = 422, message = "Create a new quote body parameters contains validation errors"),
    })
    public void addQuote(@RequestBody CreateQuoteRequestDto createQuoteRequestDto) {
        quoteService.addQuote(createQuoteRequestDto);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates the existing quote")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Quote created successfully"),
            @ApiResponse(code = 404, message = "Quote or author of the quote does not exist"),
            @ApiResponse(code = 409, message = "Quote already exists"),
            @ApiResponse(code = 422, message = "Quote body parameters contains validation errors"),
    })
    public void updateQuote(@ApiParam(name = "id", value = "Id of the quote", type = "long", required = true) @PathVariable Long id, @RequestBody UpdateQuoteRequestDto updateQuoteRequestDto) {
        quoteService.updateQuote(id, updateQuoteRequestDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes a quote by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Quote removed successfully"),
            @ApiResponse(code = 404, message = "Quote with such id does not exist")
    })
    public void deleteQuoteById(@ApiParam(name = "id", value = "Id of the quote", type = "long", required = true) @PathVariable Long id) {
        quoteService.deleteQuoteById(id);
    }
}
