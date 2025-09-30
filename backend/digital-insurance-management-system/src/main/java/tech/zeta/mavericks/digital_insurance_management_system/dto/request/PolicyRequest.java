package tech.zeta.mavericks.digital_insurance_management_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.enums.VehicleType;

import java.util.Set;

/**
 * Data Transfer Object for requesting insurance policies based on user details.
 *
 * This DTO is used to capture user attributes that affect policy eligibility
 * and premium calculations for health, vehicle, and life insurance policies.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyRequest {

    /** Indicates if the user smokes or drinks, which may affect premiums */
    private Boolean smokingDrinking;

    /** Type of the user's vehicle (e.g., car, bike) */
    private VehicleType vehicleType;

    /** Age of the user's vehicle in years */
    private Integer vehicleAge;

    /** Set of preexisting health conditions of the user */
    private Set<HealthCondition> preexistingConditions;
}
