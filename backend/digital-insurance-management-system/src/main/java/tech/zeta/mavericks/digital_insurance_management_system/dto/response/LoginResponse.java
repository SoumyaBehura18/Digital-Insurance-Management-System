package tech.zeta.mavericks.digital_insurance_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.enums.VehicleType;

import java.util.Set;

/**
 * Data Transfer Object for user login responses.
 *
 * This DTO is used to return authentication details and user information
 * after a successful login, including role, JWT token, personal, and policy-related attributes.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    /** Unique ID of the user */
    private Long userId;

    /** Role of the user (e.g., ADMIN, USER) */
    private String role;

    /** JWT authentication token for the user */
    private String token;

    /** Indicates if the user smokes or drinks */
    private Boolean smokingDrinking;

    /** Type of vehicle owned by the user */
    private VehicleType vehicleType;

    /** Age of the user's vehicle in years */
    private Integer vehicleAge;

    /** Set of preexisting health conditions of the user */
    private Set<HealthCondition> preexistingConditions;

    /** Full name of the user */
    private String name;

    /** Email address of the user */
    private String email;
}
