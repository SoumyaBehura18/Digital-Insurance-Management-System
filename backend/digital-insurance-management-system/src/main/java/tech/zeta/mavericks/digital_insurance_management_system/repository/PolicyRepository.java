package tech.zeta.mavericks.digital_insurance_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.PolicyRequest;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.PolicyWithPremiumDTO;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Policy;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.enums.VehicleType;

import java.util.List;
import java.util.Set;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

    interface PolicyCustomRepository {
        List<PolicyWithPremiumDTO> findPoliciesForUser(PolicyRequest policyRequest);

        List<PolicyWithPremiumDTO> findVehiclePoliciesForUser(PolicyRequest policyRequest);


        List<PolicyWithPremiumDTO> findLifePoliciesForUser(PolicyRequest policyRequest);


        List<PolicyWithPremiumDTO> findHealthPoliciesForUser(PolicyRequest policyRequest);
    }
}
