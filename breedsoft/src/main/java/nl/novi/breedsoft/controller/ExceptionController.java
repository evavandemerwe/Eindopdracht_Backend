package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = RecordNotFoundException.class)
    public ResponseEntity<Object> exception(RecordNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = EnumValueNotFoundException.class)
    public ResponseEntity<Object> exception(EnumValueNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DuplicateNotAllowedException.class)
    public ResponseEntity<Object> exception(DuplicateNotAllowedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = PasswordComplexityException.class)
    public ResponseEntity<Object> exception(PasswordComplexityException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = AuthorityInUseException.class)
    public ResponseEntity<Object> exception(AuthorityInUseException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(value = ZipCodeFormatException.class)
    public ResponseEntity<Object> exception(ZipCodeFormatException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.PRECONDITION_FAILED);
    }


    @ExceptionHandler(value = BadFileException.class)
    public ResponseEntity<Object> exception(BadFileException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.PRECONDITION_FAILED);
    }

}