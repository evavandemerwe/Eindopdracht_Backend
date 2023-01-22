package nl.novi.breedsoft.exception;

import java.io.Serial;

public class PasswordComplexityException extends RuntimeException{

    @Serial
    private static final long SERIAL_VERSION_UID = 1L;

    /**
     * Exception method with a form of Throwable that indicates conditions that might need to be caught
     * @param message The message given with the thrown exception
     */
    public PasswordComplexityException(String message) {
        super(message);
    }
}

