package tech.zeta.mavericks.digital_insurance_management_system.service;

import org.springframework.stereotype.Service;
import tech.zeta.mavericks.digital_insurance_management_system.entity.*;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.repository.*;

import java.util.List;
import java.util.Set;

@Service
public class PolicyServiceAdmin {

    private final PolicyRepository policyRepository;
    private final HealthPolicyPremiumRepository healthRepo;
    private final LifePolicyPremiumRepository lifeRepo;
    private final VehiclePolicyPremiumRepository vehicleRepo;
    private final HealthPreexistingConditionRepository healthPreCondRepo;

    public PolicyServiceAdmin(PolicyRepository policyRepository,
                         HealthPolicyPremiumRepository healthRepo,
                         LifePolicyPremiumRepository lifeRepo,
                         VehiclePolicyPremiumRepository vehicleRepo,
    HealthPreexistingConditionRepository healthPreCondRepo) {
        this.policyRepository = policyRepository;
        this.healthRepo = healthRepo;
        this.lifeRepo = lifeRepo;
        this.vehicleRepo = vehicleRepo;
        this.healthPreCondRepo = healthPreCondRepo;
    }

    // Create a new policy
    public Policy createPolicy(Policy policy) {
        return policyRepository.save(policy);
    }

    // Add Health Premium
    public double addHealthPremium(Long policyId, Set<HealthCondition> conditions, boolean smokingDrinking) {
        double total = 0.0;

        // base premium
        HealthPolicyPremium base = healthRepo.findFirstByPolicyId(policyId)
                .orElseThrow(() -> new RuntimeException("Base premium not found"));
        total += base.getPremiumRate();

        // smoking surcharge
        if (smokingDrinking) {
            total += 1000.0;
        }

        // disease-specific premiums from DB
        List<HealthPreexistingCondition> dbConditions =
                healthPreCondRepo.findByPolicyId(policyId);

        for (HealthPreexistingCondition dbCond : dbConditions) {
            if (conditions.contains(dbCond.getCondition())) {
                total += dbCond.getAdditionalPremium();
            }
        }
        base.setTotalPremium(total);
        healthRepo.save(base);
        return total;
    }


    // Add Life Premium
    public LifePolicyPremium addLifePremium(Long policyId, LifePolicyPremium premium) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
        premium.setPolicy(policy);
        return lifeRepo.save(premium);
    }

    // Add Vehicle Premium
    public VehiclePolicyPremium addVehiclePremium(Long policyId, VehiclePolicyPremium premium) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
        premium.setPolicy(policy);
        return vehicleRepo.save(premium);
    }
}
