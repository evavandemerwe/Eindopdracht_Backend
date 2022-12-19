package nl.novi.breedsoft.exception;

public class EnumValueNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public EnumValueNotFoundException() {
        super();
    }
    public EnumValueNotFoundException(String message) {
        super(message);
    }
}

