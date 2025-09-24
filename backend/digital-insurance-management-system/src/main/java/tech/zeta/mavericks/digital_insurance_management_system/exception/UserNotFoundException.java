package tech.zeta.mavericks.digital_insurance_management_system.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message) {
        super(message);
    }
}
