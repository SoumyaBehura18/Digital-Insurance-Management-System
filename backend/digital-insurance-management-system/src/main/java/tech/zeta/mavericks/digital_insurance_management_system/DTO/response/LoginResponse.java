package tech.zeta.mavericks.digital_insurance_management_system.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.enums.VehicleType;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private Long userId;
    private String role;
    private String token;
    private Boolean smokingDrinking;
    private VehicleType vehicleType;
    private Integer vehicleAge;
    private Set<HealthCondition> preexistingConditions;


}
