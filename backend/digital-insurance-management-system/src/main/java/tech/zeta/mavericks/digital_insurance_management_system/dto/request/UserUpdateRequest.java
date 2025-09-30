package tech.zeta.mavericks.digital_insurance_management_system.dto.request;

import jakarta.persistence.*;
import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.enums.RoleType;
import tech.zeta.mavericks.digital_insurance_management_system.enums.VehicleType;

import java.util.Set;

/**
 * Data Transfer Object for updating user details.
 *
 * This DTO is used to capture updates to user information,
 * including personal details, role, health conditions, and vehicle information.
 */
@Data
public class UserUpdateRequest {

    /** Name of the user */
    private String name;

    /** Unique email of the user */
    @Column(unique = true, nullable = false)
    private String email;

    /** Age of the user */
    private Integer age;

    /** Contact phone number of the user */
    private String phone;

    /** Role type of the user (e.g., ADMIN, USER) */
    private RoleType roleType;

    /** Residential address of the user */
    private String address;

    /** Indicates if the user smokes or drinks */
    private Boolean smokingDrinking;

    /** Set of preexisting health conditions of the user */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_preexisting_conditions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "condition")
    private Set<HealthCondition> preexistingConditions;

    /** Type of vehicle owned by the user */
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    /** Age of the user's vehicle in years */
    private Integer vehicleAge;
}
