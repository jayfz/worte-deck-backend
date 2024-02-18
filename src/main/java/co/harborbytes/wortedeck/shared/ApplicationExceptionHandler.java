package co.harborbytes.wortedeck.shared;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    final static Map<String, String> constraintsMessages;

    static {
        constraintsMessages = new HashMap<>();
        constraintsMessages.put("user_email_unique", "User.Email must be unique");
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ApiValidationError> errors = new ApiValidationErrors()
                .add(ex.getBindingResult().getFieldErrors())
                .add(ex.getBindingResult().getGlobalErrors())
                .get();

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, "validation failure");

        detail.setProperty("errors", errors);
        detail.setProperty("timestamp", Instant.now());
        detail.setProperty("outcome", "fail");
        detail.setProperty("debug", ex.getClass().getSimpleName());

        return new ResponseEntity<>(detail, status);
    }


    @ExceptionHandler
    public ResponseEntity<Object> dataIntegrityViolationHandler(DataIntegrityViolationException ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "data integrity validation failure");

        if (ex.getCause() instanceof ConstraintViolationException cve) {
            detail.setDetail(constraintsMessages.getOrDefault(cve.getConstraintName(), cve.getConstraintName()));
        }

        detail.setProperty("timestamp", Instant.now());
        detail.setProperty("outcome", "fail");
        detail.setProperty("debug", ex.getClass().getSimpleName());

        return new ResponseEntity<>(detail, HttpStatus.CONFLICT);
    }

}
