package tech.zeta.mavericks.digital_insurance_management_system.exception;

public class PolicyNotFoundException extends RuntimeException{

    public PolicyNotFoundException(String message){
        super(message);
    }
}
