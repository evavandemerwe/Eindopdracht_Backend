package nl.novi.breedsoft.exception;

public class ZipCodeFormatException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    public ZipCodeFormatException() {
        super();
    }
    public ZipCodeFormatException(String message) {
        super(message);
    }
}


