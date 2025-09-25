package tech.zeta.mavericks.digital_insurance_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.zeta.mavericks.digital_insurance_management_system.entity.UserPolicy;

@Repository
public interface UserPolicyRepository extends JpaRepository<UserPolicy, Long> {
}
