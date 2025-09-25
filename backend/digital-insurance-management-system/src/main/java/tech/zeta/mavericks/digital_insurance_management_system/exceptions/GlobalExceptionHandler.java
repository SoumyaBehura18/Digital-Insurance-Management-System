//package tech.zeta.mavericks.digital_insurance_management_system.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import tech.zeta.mavericks.digital_insurance_management_system.exceptions.ClaimSubmissionException;
//import tech.zeta.mavericks.digital_insurance_management_system.exceptions.UserPolicyNotFoundException;
//import tech.zeta.mavericks.digital_insurance_management_system.exception.ValidationErrorResponse;
//import tech.zeta.mavericks.digital_insurance_management_system.exception.ErrorResponse;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler2 {
//
//    @ExceptionHandler(UserPolicyNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleUserPolicyNotFound(UserPolicyNotFoundException ex) {
//        ErrorResponse error = new ErrorResponse(
//                "USER_POLICY_NOT_FOUND",
//                ex.getMessage(),
//                LocalDateTime.now()
//        );
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(ClaimSubmissionException.class)
//    public ResponseEntity<ErrorResponse> handleClaimSubmissionException(ClaimSubmissionException ex) {
//        ErrorResponse error = new ErrorResponse(
//                "CLAIM_SUBMISSION_ERROR",
//                ex.getMessage(),
//                LocalDateTime.now()
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//
//        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
//                "VALIDATION_ERROR",
//                "Invalid input data",
//                errors,
//                LocalDateTime.now()
//        );
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
//        ErrorResponse error = new ErrorResponse(
//                "INTERNAL_SERVER_ERROR",
//                "An unexpected error occurred",
//                LocalDateTime.now()
//        );
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}
