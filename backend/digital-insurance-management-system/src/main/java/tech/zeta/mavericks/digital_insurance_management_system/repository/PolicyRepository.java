package tech.zeta.mavericks.digital_insurance_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Policy;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.enums.VehicleType;

import java.util.List;
import java.util.Set;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

    interface PolicyCustomRepository {
        List<tech.zeta.mavericks.digital_insurance_management_system.dto.PolicyWithPremiumDTO> findPoliciesForUser(Boolean smokingDrinking,
                                                                                                                   VehicleType vehicleType,
                                                                                                                   Integer vehicleAge,
                                                                                                                   Set<HealthCondition> preexistingConditions);
    }
}
