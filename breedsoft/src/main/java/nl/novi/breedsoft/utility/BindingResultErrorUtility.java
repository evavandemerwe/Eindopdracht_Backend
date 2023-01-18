package nl.novi.breedsoft.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class BindingResultErrorUtility {

    /**
     * A method that returns a Bad Request HTTP message containing all HTTP request
     * input parameters that have invalid values.
     * @param bindingResult list errors of input parameters
     * @return Bad Request object with messages containing invalid input data
     */
    public static ResponseEntity<Object> bindingResultError(BindingResult bindingResult){
        StringBuilder sb = new StringBuilder();
        for (FieldError fe : bindingResult.getFieldErrors()) {
            sb.append(fe.getField()).append(": ");
            sb.append(fe.getDefaultMessage());
            sb.append("\n");
        }
        return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
    }
}
