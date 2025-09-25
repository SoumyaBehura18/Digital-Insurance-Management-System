package tech.zeta.mavericks.digital_insurance_management_system.repository.interfaces;

import tech.zeta.mavericks.digital_insurance_management_system.dto.PolicyWithPremiumDTO;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.enums.VehicleType;

import java.util.List;
import java.util.Set;

public interface PolicyCustomRepository {
    List<PolicyWithPremiumDTO> findPoliciesForUser(Boolean smokingDrinking,
                                                   VehicleType vehicleType,
                                                   Integer vehicleAge,
                                                   Set<HealthCondition> preexistingConditions);
}
