package nl.novi.breedsoft.exception;

public class DuplicateNotAllowedException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public DuplicateNotAllowedException() {
        super();
    }
    public DuplicateNotAllowedException(String message) {
        super(message);
    }
}

