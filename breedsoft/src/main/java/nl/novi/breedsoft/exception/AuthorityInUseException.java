package nl.novi.breedsoft.exception;

import java.io.Serial;

public class AuthorityInUseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Exception method with a form of Throwable that indicates conditions that might need to be caught
     * @param message The message given with the thrown exception
     */
    public AuthorityInUseException(String message) {
        super(message);
    }
}

