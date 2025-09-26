package tech.zeta.mavericks.digital_insurance_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.zeta.mavericks.digital_insurance_management_system.entity.HealthPreexistingCondition;

import java.util.List;

// HealthPreexistingConditionRepository
public interface HealthPreexistingConditionRepository extends JpaRepository<HealthPreexistingCondition, Long> {
     List<HealthPreexistingCondition> findByPolicyId(Long policyId);
}