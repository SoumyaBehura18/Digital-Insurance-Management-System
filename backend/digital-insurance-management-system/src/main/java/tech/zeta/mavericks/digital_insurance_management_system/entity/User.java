package tech.zeta.mavericks.digital_insurance_management_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.enums.RoleType;
import tech.zeta.mavericks.digital_insurance_management_system.enums.VehicleType;

import java.util.Set;

/**
 * Entity representing a user in the insurance management system.
 *
 * Stores personal information, role, contact details, vehicle information,
 * preexisting health conditions, and account credentials.
 */
@Entity
@Table(name = "users")
@Data
public class User {

    /** Unique ID of the user */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Full name of the user */
    private String name;

    /** Unique email address of the user */
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

    /** Encrypted password of the user */
    private String password;

    /** Indicates whether the user smokes or drinks */
    private Boolean smokingDrinking;

    /** Set of preexisting health conditions of the user */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_preexisting_conditions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "condition")
    private Set<HealthCondition> preexistingConditions;

    /** Type of vehicle owned by the user */
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    /** Age of the user's vehicle */
    private Integer vehicleAge;
}
