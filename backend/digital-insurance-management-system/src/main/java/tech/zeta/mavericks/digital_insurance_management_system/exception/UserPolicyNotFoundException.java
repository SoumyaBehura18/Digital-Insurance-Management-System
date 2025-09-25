package tech.zeta.mavericks.digital_insurance_management_system.exception;

public class UserPolicyNotFoundException extends RuntimeException{
    public UserPolicyNotFoundException(String message){
        super(message);
    }
}
