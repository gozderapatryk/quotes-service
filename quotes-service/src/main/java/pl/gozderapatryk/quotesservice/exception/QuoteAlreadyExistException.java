package pl.gozderapatryk.quotesservice.exception;

public class QuoteAlreadyExistException extends RuntimeException {

    private static final String defaultMessage = "Quote already exists.";

    public QuoteAlreadyExistException() {
        super(defaultMessage);
    }

    public QuoteAlreadyExistException(String message) {
        super(message);
    }
}
