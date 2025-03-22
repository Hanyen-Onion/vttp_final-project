package vttp.batch_b.min_project.server.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vttp.batch_b.min_project.server.models.ErrorObject;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(QueryNotReceivedException.class)
    public ResponseEntity<ErrorObject> handleQueryNotFound(
        Exception ex, HttpServletRequest req, HttpServletResponse resp) {
        
        ErrorObject err = new ErrorObject(ex.getMessage(), resp.getStatus(), new Date(), "");

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AirportNotFoundException.class)
    public ResponseEntity<ErrorObject> handleAiportNotFound(
        Exception ex, HttpServletRequest req, HttpServletResponse resp) {

        ErrorObject err = new ErrorObject(ex.getMessage(), resp.getStatus(), new Date(), "");

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }
}
