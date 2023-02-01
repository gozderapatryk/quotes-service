package pl.gozderapatryk.quotesservice.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.gozderapatryk.quotesservice.dto.request.CreateAuthorRequestDto;
import pl.gozderapatryk.quotesservice.dto.response.GetAuthorDto;
import pl.gozderapatryk.quotesservice.dto.response.FindAuthorsResponseDto;
import pl.gozderapatryk.quotesservice.service.AuthorService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    @ApiOperation(value = "Retrieves a list of authors")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieved a list of authors")
    })
    public FindAuthorsResponseDto findAuthors(
            @ApiParam(name = "page", value = "Results page you want to retrieve (0...N)", type = "integer", required = true, defaultValue = "1") @RequestParam(defaultValue = "0") int page,
            @ApiParam(name = "limit", value = "Number of records per page ", type = "integer",  required = true, defaultValue = "20") @RequestParam(defaultValue = "20") int limit,
            @ApiParam(name = "sort", value = "Sorting direction of authors (asc or desc)", type = "string", required = true, defaultValue = "asc") @RequestParam(defaultValue = "asc") String sort,
            @ApiParam(name = "author", value = "String that is contained in first name, middle name or last name of author.", type = "string") @RequestParam(required = false) String author
    ) {
        return authorService.findAuthors(page, limit, sort, author);
    }

    @PostMapping
    @ApiOperation(value = "Creates a new author")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Author created successfully"),
            @ApiResponse(code = 409, message = "Author with such name already exists"),
            @ApiResponse(code = 422, message = "Create a new author body parameters contains validation errors"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    public GetAuthorDto createAuthor(@RequestBody CreateAuthorRequestDto createAuthorRequestDto) {
        return authorService.createAuthor(createAuthorRequestDto);
    }

    @DeleteMapping("/{id}/quotes")
    @ApiOperation(value = "Deletes all quotes by author's id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Author removed successfully"),
            @ApiResponse(code = 404, message = "Author with such id does not exist")
    })
    public void deleteQuotesByAuthorId(@ApiParam(name = "id", value = "Id of the author", type = "long", required = true) @PathVariable Long id) {
        authorService.deleteQuotesByAuthorId(id);
    }
}
