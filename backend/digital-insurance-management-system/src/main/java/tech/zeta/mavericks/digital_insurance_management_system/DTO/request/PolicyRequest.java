package tech.zeta.mavericks.digital_insurance_management_system.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.enums.VehicleType;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyRequest {
    private Boolean smokingDrinking;                // true if user smokes or drinks
    private VehicleType vehicleType;               // type of vehicle
    private Integer vehicleAge;                    // age of the vehicle
    private Set<HealthCondition> preexistingConditions;  // preexisting diseases
}

