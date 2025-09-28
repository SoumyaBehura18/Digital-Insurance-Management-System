package tech.zeta.mavericks.digital_insurance_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.premium.ConditionPremiumDTO;
import tech.zeta.mavericks.digital_insurance_management_system.entity.*;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.exception.PolicyNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.repository.*;

import java.util.List;
import java.util.Set;

@Service
public class PolicyServiceAdmin {

    @Autowired private PolicyRepository policyRepository;
   @Autowired private final HealthPolicyPremiumRepository healthRepo;
   @Autowired final LifePolicyPremiumRepository lifeRepo;
   @Autowired private final VehiclePolicyPremiumRepository vehicleRepo;
   @Autowired private final HealthPreexistingConditionRepository healthPreCondRepo;

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
    public double addHealthPremium(Long policyId, Set<ConditionPremiumDTO> conditions,
                                   Double premiumRate, Double renewalRate) {
        double total = 0.0;

        // fetch base policy
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found with id: " + policyId));

        // Non-smoker premium
        HealthPolicyPremium baseFalse = new HealthPolicyPremium();
        baseFalse.setPolicy(policy);
        baseFalse.setPremiumRate(premiumRate);
        baseFalse.setRenewalRate(renewalRate);
        baseFalse.setSmokingDrinking(false);
        healthRepo.save(baseFalse);

        // Smoker premium
        HealthPolicyPremium baseTrue = new HealthPolicyPremium();
        baseTrue.setPolicy(policy);
        baseTrue.setPremiumRate(premiumRate + 1000);
        baseTrue.setRenewalRate(renewalRate + 1000);
        baseTrue.setSmokingDrinking(true);
        healthRepo.save(baseTrue);

        // Save pre-existing conditions
        addPreexistingCondition(policyId, conditions);

        return total;
    }


    public double addPreexistingCondition(Long policyId, Set<ConditionPremiumDTO> conditionPremiums) {
        double total = 0.0;

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found"));

        for (ConditionPremiumDTO dto : conditionPremiums) {
            HealthPreexistingCondition preexisting = new HealthPreexistingCondition();
            preexisting.setPolicy(policy);
            preexisting.setCondition(HealthCondition.valueOf(dto.getCondition().toString().toUpperCase())); // enum
            preexisting.setAdditionalPremium(dto.getExtraPremium());
            healthPreCondRepo.save(preexisting);
        }

        return total;
    }


    // Add Life Premium
    public double addLifePremium(Long policyId, Double premiumRate, Double renewalRate) {
        double total = 0.0;

        // fetch base premium from DB
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found with id: " + policyId));

        // create base premium for non-smokers
       LifePolicyPremium baseFalse1 = new LifePolicyPremium();
        baseFalse1.setPolicy(policy);                 // set policyId
        baseFalse1.setPremiumRate(premiumRate);       // set premium rate
        baseFalse1.setRenewalRate(renewalRate);       // set renewal rate
        baseFalse1.setSmokingDrinking(false);

        LifePolicyPremium baseTrue1 = new LifePolicyPremium();
        baseTrue1.setPolicy(policy);                 // set policyId
        baseTrue1.setPremiumRate(premiumRate+1000);       // set premium rate
        baseTrue1.setRenewalRate(renewalRate+1000);       // set renewal rate
        baseTrue1.setSmokingDrinking(true);
        // save it
        lifeRepo.save(baseFalse1);
        lifeRepo.save(baseTrue1);


        return total;
    }


    // Add Vehicle Premium
    public double addVehiclePremium(Long policyId, Double premiumRate, Double renewalRate) {
        double total = 0.0;

        // fetch base policy
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found with id: " + policyId));

        // vehicle age <= 5 years
        VehiclePolicyPremium base = new VehiclePolicyPremium();
        base.setPolicy(policy);
        base.setPremiumRate(premiumRate);
        base.setRenewalRate(renewalRate);
        base.setVehicleAge(5);  // mark max age in this slab
        vehicleRepo.save(base);

        return total;
    }

    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }


}
