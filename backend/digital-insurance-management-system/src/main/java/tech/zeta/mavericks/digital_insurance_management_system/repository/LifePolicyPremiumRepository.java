package tech.zeta.mavericks.digital_insurance_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.zeta.mavericks.digital_insurance_management_system.entity.LifePolicyPremium;

import java.util.Optional;

public interface LifePolicyPremiumRepository extends JpaRepository<LifePolicyPremium, Long> {
    Optional<Object> findFirstByPolicyId(Long policyId);
}