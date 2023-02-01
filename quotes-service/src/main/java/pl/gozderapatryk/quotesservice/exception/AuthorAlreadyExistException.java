package pl.gozderapatryk.quotesservice.exception;

public class AuthorAlreadyExistException extends RuntimeException {

    private static final String defaultMessage = "Author already exists.";

    public AuthorAlreadyExistException() {
        super(defaultMessage);
    }

    public AuthorAlreadyExistException(String message) {
        super(message);
    }
}
