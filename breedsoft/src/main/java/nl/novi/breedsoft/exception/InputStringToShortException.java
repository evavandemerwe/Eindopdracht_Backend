package nl.novi.breedsoft.exception;

public class InputStringToShortException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public InputStringToShortException() {
        super();
    }
    public InputStringToShortException(String message) {
        super(message);
    }
}