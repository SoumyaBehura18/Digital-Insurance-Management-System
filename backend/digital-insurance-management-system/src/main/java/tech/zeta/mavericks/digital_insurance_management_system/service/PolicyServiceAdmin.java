package tech.zeta.mavericks.digital_insurance_management_system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.PolicyAdminResponse;
import tech.zeta.mavericks.digital_insurance_management_system.entity.*;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.exception.PolicyNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.repository.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for administrative operations on policies.
 * Handles creating policies and adding premiums for health, life, and vehicle insurance.
 */
@Service
public class PolicyServiceAdmin {

    private static final Logger logger = LoggerFactory.getLogger(PolicyServiceAdmin.class);

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private final HealthPolicyPremiumRepository healthRepo;

    @Autowired
    final LifePolicyPremiumRepository lifeRepo;

    @Autowired
    private final VehiclePolicyPremiumRepository vehicleRepo;

    @Autowired
    private final HealthPreexistingConditionRepository healthPreCondRepo;

    /**
     * Constructor with dependency injection for repositories.
     */
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

    /**
     * Creates a new policy.
     *
     * @param policy the policy entity to save
     * @return the saved policy
     */
    public Policy createPolicy(Policy policy) {
        logger.info("Creating new policy: {}", policy.getName());
        return policyRepository.save(policy);
    }

    /**
     * Adds health premiums for a policy, including smoker and non-smoker rates.
     * Also adds pre-existing conditions if provided.
     *
     * @param policyId the ID of the policy
     * @param conditions the pre-existing conditions
     * @param premiumRate base premium rate
     * @param renewalRate base renewal rate
     * @return total premium (currently 0, can be calculated if needed)
     */
    public double addHealthPremium(Long policyId, Set<tech.zeta.mavericks.digital_insurance_management_system.dto.premium.ConditionPremium> conditions,
                                   Double premiumRate, Double renewalRate) {
        logger.info("Adding health premium for policy ID: {}", policyId);

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> {
                    logger.warn("Policy not found with ID: {}", policyId);
                    return new PolicyNotFoundException("Policy not found with id: " + policyId);
                });

        // Non-smoker premium
        HealthPolicyPremium baseFalse = new HealthPolicyPremium();
        baseFalse.setPolicy(policy);
        baseFalse.setPremiumRate(premiumRate);
        baseFalse.setRenewalRate(renewalRate);
        baseFalse.setSmokingDrinking(false);
        healthRepo.save(baseFalse);
        logger.info("Saved non-smoker health premium for policy ID: {}", policyId);

        // Smoker premium
        HealthPolicyPremium baseTrue = new HealthPolicyPremium();
        baseTrue.setPolicy(policy);
        baseTrue.setPremiumRate(premiumRate + 1000);
        baseTrue.setRenewalRate(renewalRate + 1000);
        baseTrue.setSmokingDrinking(true);
        healthRepo.save(baseTrue);
        logger.info("Saved smoker health premium for policy ID: {}", policyId);

        // Save pre-existing conditions
        addPreexistingCondition(policyId, conditions);

        return 0.0;
    }

    /**
     * Adds pre-existing conditions to a health policy.
     *
     * @param policyId the ID of the policy
     * @param conditionPremiums set of condition premiums
     * @return total additional premium (currently 0, can be calculated if needed)
     */
    public double addPreexistingCondition(Long policyId, Set<tech.zeta.mavericks.digital_insurance_management_system.dto.premium.ConditionPremium> conditionPremiums) {
        logger.info("Adding pre-existing conditions for policy ID: {}", policyId);

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> {
                    logger.warn("Policy not found with ID: {}", policyId);
                    return new PolicyNotFoundException("Policy not found");
                });

        for (var dto : conditionPremiums) {
            HealthPreexistingCondition preexisting = new HealthPreexistingCondition();
            preexisting.setPolicy(policy);
            preexisting.setCondition(HealthCondition.valueOf(dto.getCondition().toString().toUpperCase()));
            preexisting.setAdditionalPremium(dto.getExtraPremium());
            healthPreCondRepo.save(preexisting);
            logger.info("Saved pre-existing condition {} for policy ID: {}", dto.getCondition(), policyId);
        }

        return 0.0;
    }

    /**
     * Adds life insurance premium for a policy including smoker and non-smoker rates.
     *
     * @param policyId the ID of the policy
     * @param premiumRate base premium rate
     * @param renewalRate base renewal rate
     * @return total premium (currently 0)
     */
    public double addLifePremium(Long policyId, Double premiumRate, Double renewalRate) {
        logger.info("Adding life premium for policy ID: {}", policyId);

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> {
                    logger.warn("Policy not found with ID: {}", policyId);
                    return new PolicyNotFoundException("Policy not found with id: " + policyId);
                });

        LifePolicyPremium baseFalse = new LifePolicyPremium();
        baseFalse.setPolicy(policy);
        baseFalse.setPremiumRate(premiumRate);
        baseFalse.setRenewalRate(renewalRate);
        baseFalse.setSmokingDrinking(false);

        LifePolicyPremium baseTrue = new LifePolicyPremium();
        baseTrue.setPolicy(policy);
        baseTrue.setPremiumRate(premiumRate + 1000);
        baseTrue.setRenewalRate(renewalRate + 1000);
        baseTrue.setSmokingDrinking(true);

        lifeRepo.save(baseFalse);
        lifeRepo.save(baseTrue);

        logger.info("Saved life premiums for policy ID: {}", policyId);
        return 0.0;
    }

    /**
     * Adds vehicle insurance premium for a policy.
     *
     * @param policyId the ID of the policy
     * @param premiumRate base premium rate
     * @param renewalRate renewal rate
     * @param vehicleAge age of the vehicle
     * @return total premium (currently 0)
     */
    public double addVehiclePremium(Long policyId, Double premiumRate, Double renewalRate, Integer vehicleAge) {
        logger.info("Adding vehicle premium for policy ID: {}", policyId);

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> {
                    logger.warn("Policy not found with ID: {}", policyId);
                    return new PolicyNotFoundException("Policy not found with id: " + policyId);
                });

        VehiclePolicyPremium vehiclePremium = new VehiclePolicyPremium();
        vehiclePremium.setPolicy(policy);
        vehiclePremium.setPremiumRate(premiumRate);
        vehiclePremium.setRenewalRate(renewalRate);
        vehiclePremium.setVehicleAge(vehicleAge);

        vehicleRepo.save(vehiclePremium);
        logger.info("Saved vehicle premium for policy ID: {}", policyId);

        return 0.0;
    }

    /**
     * Retrieves all policies and maps them to PolicyAdminResponse DTOs.
     *
     * @return list of PolicyAdminResponse objects
     */
    public List<PolicyAdminResponse> getAllPolicies() {
        logger.info("Fetching all policies");
        List<Policy> policies = policyRepository.findAll();

        return policies.stream()
                .map(policy -> new PolicyAdminResponse(
                        policy.getId(),
                        policy.getName(),
                        policy.getDescription(),
                        policy.getType(),
                        policy.getDurationMonths(),
                        policy.getCoverageAmt()
                ))
                .collect(Collectors.toList());
    }
}
