package pl.gozderapatryk.quotesservice.exception;

public class QuoteNotFoundException extends RuntimeException {

    private static final String template = "Quote with the following id: %s does not exist.";

    public QuoteNotFoundException(Long id) {
        super(String.format(template, id));
    }
}
