package nl.novi.breedsoft.exception;

public class PasswordComplexityException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    public PasswordComplexityException() {
        super();
    }
    public PasswordComplexityException(String message) {
        super(message);
    }
}

