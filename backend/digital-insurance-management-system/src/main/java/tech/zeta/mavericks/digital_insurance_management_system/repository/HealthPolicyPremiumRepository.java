package tech.zeta.mavericks.digital_insurance_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.zeta.mavericks.digital_insurance_management_system.entity.HealthPolicyPremium;

import java.util.Optional;

public interface HealthPolicyPremiumRepository extends JpaRepository<HealthPolicyPremium, Long> {
    Optional<HealthPolicyPremium> findFirstByPolicyId(Long policyId);
}