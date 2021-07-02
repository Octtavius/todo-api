package home.ie.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@RestController
public class RestResponseEntityExceptionHandler
        extends  ResponseEntityExceptionHandler {

    @ExceptionHandler(TodoNotFoundException.class)
    public final ResponseEntity<ExceptionResponse>
    todoNotFound(TodoNotFoundException ex) {

        ExceptionResponse exceptionResponse =
                new ExceptionResponse(  ex.getMessage(),
                        "Any details you would want to add");

        return new ResponseEntity<ExceptionResponse>
                (exceptionResponse, new HttpHeaders(),
                        HttpStatus.NOT_FOUND);

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        ExceptionResponse error = new ExceptionResponse("Failed to validate parameters", "validation exception detials");
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

}
