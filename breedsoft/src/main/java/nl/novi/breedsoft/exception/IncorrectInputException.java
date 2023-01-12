package nl.novi.breedsoft.exception;

public class IncorrectInputException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public IncorrectInputException() {
        super();
    }
    public IncorrectInputException(String message) {
        super(message);
    }
}