package tech.zeta.mavericks.digital_insurance_management_system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.PolicyRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.PolicyWithPremium;
import tech.zeta.mavericks.digital_insurance_management_system.repository.PolicyRepository;

import java.util.List;

/**
 * Service class responsible for fetching insurance policies for users.
 * Supports retrieving all policies as well as specific types: vehicle, life, and health.
 */
@Service
public class PolicyService {

    private static final Logger logger = LoggerFactory.getLogger(PolicyService.class);

    private final PolicyRepository.PolicyCustomRepository policyRepo;

    /**
     * Constructor for PolicyService with dependency injection of custom policy repository.
     *
     * @param policyRepo the custom policy repository
     */
    public PolicyService(PolicyRepository.PolicyCustomRepository policyRepo) {
        this.policyRepo = policyRepo;
    }

    /**
     * Retrieves all policies for a given user based on the provided request.
     *
     * @param policyRequest the request containing filter details
     * @return list of policies with premium details
     */
    public List<PolicyWithPremium> getPoliciesForUser(PolicyRequest policyRequest) {
        logger.info("Fetching all policies for request: {}", policyRequest);
        return policyRepo.findPoliciesForUser(policyRequest);
    }

    /**
     * Retrieves vehicle policies for a given user.
     *
     * @param policyRequest the request containing filter details
     * @return list of vehicle policies with premium details
     */
    public List<PolicyWithPremium> getVehiclePoliciesForUser(PolicyRequest policyRequest) {
        logger.info("Fetching vehicle policies for request: {}", policyRequest);
        return policyRepo.findVehiclePoliciesForUser(policyRequest);
    }

    /**
     * Retrieves life policies for a given user.
     *
     * @param policyRequest the request containing filter details
     * @return list of life policies with premium details
     */
    public List<PolicyWithPremium> getLifePoliciesForUser(PolicyRequest policyRequest) {
        logger.info("Fetching life policies for request: {}", policyRequest);
        return policyRepo.findLifePoliciesForUser(policyRequest);
    }

    /**
     * Retrieves health policies for a given user.
     *
     * @param policyRequest the request containing filter details
     * @return list of health policies with premium details
     */
    public List<PolicyWithPremium> getHealthPoliciesForUser(PolicyRequest policyRequest) {
        logger.info("Fetching health policies for request: {}", policyRequest);
        return policyRepo.findHealthPoliciesForUser(policyRequest);
    }
}
