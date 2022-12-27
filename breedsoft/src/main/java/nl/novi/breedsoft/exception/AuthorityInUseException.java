package nl.novi.breedsoft.exception;

public class AuthorityInUseException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public AuthorityInUseException() {
        super();
    }
    public AuthorityInUseException(String message) {
        super(message);
    }
}

