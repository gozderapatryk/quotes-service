package pl.gozderapatryk.quotesservice.exception;

public class AuthorNotFoundException extends RuntimeException {
    private static final String template = "Author with the following id: %s does not exist.";

    public AuthorNotFoundException(Long id) {
        super(String.format(template, id));
    }
}
