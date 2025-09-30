package tech.zeta.mavericks.digital_insurance_management_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the Digital Insurance Management System.
 *
 * Catches specific exceptions thrown across controllers and returns
 * standardized HTTP responses with appropriate status codes.
 *
 * Handled exceptions:
 * - UserNotFoundException       : Returns 404 NOT_FOUND
 * - PolicyNotFoundException     : Returns 404 NOT_FOUND
 * - ClaimNotFoundException      : Returns 404 NOT_FOUND
 * - TicketNotFoundException     : Returns 404 NOT_FOUND
 * - UserPolicyNotFoundException : Returns 404 NOT_FOUND
 * - UserAlreadyExistException   : Returns 302 FOUND
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PolicyNotFoundException.class)
    public ResponseEntity<String> handlePolicyNotFoundException(PolicyNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ClaimNotFoundException.class)
    public ResponseEntity<String> handleClaimNotFoundException(ClaimNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<String> handleTicketNotFoundException(TicketNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserPolicyNotFoundException.class)
    public ResponseEntity<String> handleUserPolicyNotFoundException(UserPolicyNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<String> handleUserAlreadyExistException(UserAlreadyExistException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FOUND);
    }
}
