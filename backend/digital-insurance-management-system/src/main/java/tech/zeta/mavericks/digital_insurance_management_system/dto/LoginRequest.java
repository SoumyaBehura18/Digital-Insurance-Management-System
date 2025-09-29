package tech.zeta.mavericks.digital_insurance_management_system.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
