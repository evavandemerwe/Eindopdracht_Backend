package nl.novi.breedsoft.exception;

public class BadFileException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public BadFileException() {
        super();
    }
    public BadFileException(String message) {
        super(message);
    }
}