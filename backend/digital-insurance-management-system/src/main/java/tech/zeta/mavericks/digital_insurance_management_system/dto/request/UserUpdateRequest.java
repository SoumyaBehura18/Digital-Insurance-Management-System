package tech.zeta.mavericks.digital_insurance_management_system.dto.request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.enums.RoleType;
import tech.zeta.mavericks.digital_insurance_management_system.enums.VehicleType;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private Integer age;
    private String phone;
    private RoleType roleType;
    private String address;
    private Boolean smokingDrinking;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_preexisting_conditions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "condition")
    private Set<HealthCondition> preexistingConditions;
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
    private Integer vehicleAge;
}
