package tech.zeta.mavericks.digital_insurance_management_system.dto;

import lombok.Data;

/**
 * Data Transfer Object for user login requests.
 *
 * This DTO is used to capture the user's email and password
 * when attempting to authenticate in the system.
 */
@Data
public class LoginRequest {

    /** Email address of the user attempting to log in */
    private String email;

    /** Password of the user */
    private String password;
}
