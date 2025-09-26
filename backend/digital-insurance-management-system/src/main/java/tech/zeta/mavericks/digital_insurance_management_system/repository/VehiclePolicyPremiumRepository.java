package tech.zeta.mavericks.digital_insurance_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.zeta.mavericks.digital_insurance_management_system.entity.VehiclePolicyPremium;

public interface VehiclePolicyPremiumRepository extends JpaRepository<VehiclePolicyPremium, Long> {
}