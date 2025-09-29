package tech.zeta.mavericks.digital_insurance_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.PolicyRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.PolicyWithPremium;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Policy;

import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

    interface PolicyCustomRepository {
        List<PolicyWithPremium> findPoliciesForUser(PolicyRequest policyRequest);

        List<PolicyWithPremium> findVehiclePoliciesForUser(PolicyRequest policyRequest);


        List<PolicyWithPremium> findLifePoliciesForUser(PolicyRequest policyRequest);


        List<PolicyWithPremium> findHealthPoliciesForUser(PolicyRequest policyRequest);
    }
}
