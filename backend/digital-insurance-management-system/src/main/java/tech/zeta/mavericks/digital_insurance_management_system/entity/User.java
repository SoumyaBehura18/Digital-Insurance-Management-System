package tech.zeta.mavericks.digital_insurance_management_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.enums.RoleType;
import tech.zeta.mavericks.digital_insurance_management_system.enums.VehicleType;

import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private Integer age;
    private String phone;
    private RoleType roleType;
    private String address;
    private String password;
    private Boolean smokingDrinking;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_preexisting_conditions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "condition")
    private Set<HealthCondition> preexistingConditions;
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
    private Integer vehicleAge;
}
