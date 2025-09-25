package tech.zeta.mavericks.digital_insurance_management_system.exception;

public class ClaimNotFoundException extends RuntimeException{

    public ClaimNotFoundException(String message){
        super(message);
    }
}
