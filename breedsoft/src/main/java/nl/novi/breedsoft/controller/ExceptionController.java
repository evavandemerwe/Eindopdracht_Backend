package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    /**
     * Exception handler for record not found exception
     * @param exception that is thrown
     * @return Response entity with exception message and http status not found
     */
    @ExceptionHandler(value = RecordNotFoundException.class)
    public ResponseEntity<Object> exception(RecordNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Exception handler for enum value not found exception
     * @param exception that is thrown
     * @return Response entity with exception message and http status not found
     */
    @ExceptionHandler(value = EnumValueNotFoundException.class)
    public ResponseEntity<Object> exception(EnumValueNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Exception handler for duplicate not allowed exception
     * @param exception that is thrown
     * @return Response entity with exception message and http status conflict
     */
    @ExceptionHandler(value = DuplicateNotAllowedException.class)
    public ResponseEntity<Object> exception(DuplicateNotAllowedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Exception handler for password complexity exception
     * @param exception that is thrown
     * @return Response entity with exception message and http status conflict
     */
    @ExceptionHandler(value = PasswordComplexityException.class)
    public ResponseEntity<Object> exception(PasswordComplexityException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Exception handler for authority in use exception
     * @param exception that is thrown
     * @return Response entity with exception message and http status precondition failed
     */
    @ExceptionHandler(value = AuthorityInUseException.class)
    public ResponseEntity<Object> exception(AuthorityInUseException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.PRECONDITION_FAILED);
    }

    /**
     * Exception handler for zipcode format exception
     * @param exception that is thrown
     * @return Response entity with exception message and http status precondition failed
     */
    @ExceptionHandler(value = ZipCodeFormatException.class)
    public ResponseEntity<Object> exception(ZipCodeFormatException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.PRECONDITION_FAILED);
    }

    /**
     * Exception handler for bad file exception
     * @param exception that is thrown
     * @return Response entity with exception message and http status precondition failed
     */
    @ExceptionHandler(value = BadFileException.class)
    public ResponseEntity<Object> exception(BadFileException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.PRECONDITION_FAILED);
    }

    /**
     * Exception handler for incorrect input exception
     * @param exception that is thrown
     * @return Response entity with exception message and http status precondition failed
     */
    @ExceptionHandler(value = IncorrectInputException.class)
    public ResponseEntity<Object> exception(IncorrectInputException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.PRECONDITION_FAILED);
    }

}