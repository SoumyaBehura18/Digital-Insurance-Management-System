package tech.zeta.mavericks.digital_insurance_management_system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.UserPolicyRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.UserPolicyResponse;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Policy;
import tech.zeta.mavericks.digital_insurance_management_system.entity.User;
import tech.zeta.mavericks.digital_insurance_management_system.entity.UserPolicy;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyStatus;
import tech.zeta.mavericks.digital_insurance_management_system.exception.PolicyNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.exception.UserNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.repository.PolicyRepository;
import tech.zeta.mavericks.digital_insurance_management_system.repository.UserPolicyRepository;
import tech.zeta.mavericks.digital_insurance_management_system.repository.UserRepository;

import java.util.List;

/**
 * Service class to manage User Policies.
 * Handles creating, updating, retrieving, and mapping user policy data.
 */
@Service
public class UserPolicyService {

    private static final Logger logger = LoggerFactory.getLogger(UserPolicyService.class);

    @Autowired
    private UserPolicyRepository userPolicyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PolicyRepository policyRepository;

    /**
     * Saves a new UserPolicy based on the request data.
     *
     * @param request the UserPolicyRequest DTO
     * @return the saved UserPolicy mapped to a response DTO
     */
    public UserPolicyResponse saveUserPolicy(UserPolicyRequest request) {
        logger.info("Saving user policy for user ID: {} and policy ID: {}", request.getUserId(), request.getPolicyId());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> {
                    logger.warn("User not found with ID: {}", request.getUserId());
                    return new UserNotFoundException("User not found");
                });

        Policy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> {
                    logger.warn("Policy not found with ID: {}", request.getPolicyId());
                    return new PolicyNotFoundException("Policy not found");
                });

        UserPolicy userPolicy = new UserPolicy();
        userPolicy.setUser(user);
        userPolicy.setPolicy(policy);
        userPolicy.setStartDate(request.getStartDate());
        userPolicy.setEndDate(request.getEndDate());
        userPolicy.setStatus(PolicyStatus.valueOf(request.getStatus()));
        userPolicy.setPremiumPaid(request.getPremiumPaid());
        userPolicy.setNoClaimBonus(false);

        UserPolicy saved = userPolicyRepository.save(userPolicy);
        logger.info("User policy saved with ID: {}", saved.getId());

        return mapToResponse(saved);
    }

    /**
     * Retrieves all policies for a given user.
     *
     * @param userId the user's ID
     * @return list of UserPolicyResponse DTOs
     */
    public List<UserPolicyResponse> getUserPoliciesByUserId(Long userId) {
        logger.info("Fetching policies for user ID: {}", userId);

        List<UserPolicy> policies = userPolicyRepository.findByUserId(userId);
        if (policies.isEmpty()) {
            logger.warn("No policies found for user ID: {}", userId);
            throw new PolicyNotFoundException("No policies found for user with ID " + userId);
        }

        return policies.stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Retrieves a UserPolicy by its ID.
     *
     * @param id the UserPolicy ID
     * @return the UserPolicyResponse DTO
     */
    public UserPolicyResponse getUserPolicyById(Long id) {
        logger.info("Fetching user policy by ID: {}", id);
        UserPolicy userPolicy = userPolicyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("UserPolicy not found with ID: {}", id);
                    return new PolicyNotFoundException("UserPolicy not found with id: " + id);
                });

        return mapToResponse(userPolicy);
    }

    /**
     * Updates the noClaimBonus flag for a UserPolicy.
     *
     * @param id the UserPolicy ID
     * @return the updated UserPolicyResponse DTO
     */
    public UserPolicyResponse updateUserPolicyById(Long id) {
        logger.info("Updating noClaimBonus for UserPolicy ID: {}", id);

        UserPolicy userPolicy = userPolicyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("UserPolicy not found with ID: {}", id);
                    return new PolicyNotFoundException("UserPolicy not found with id: " + id);
                });

        userPolicy.setNoClaimBonus(true);
        UserPolicy updated = userPolicyRepository.save(userPolicy);
        logger.info("UserPolicy ID {} updated with noClaimBonus=true", id);

        return mapToResponse(updated);
    }

    /**
     * Updates the status and premium paid for a UserPolicy.
     *
     * @param id          the UserPolicy ID
     * @param policyStatus the new policy status
     * @param premiumRate  the updated premium
     * @return the updated UserPolicyResponse DTO
     */
    public UserPolicyResponse updateUserPolicyStatusById(Long id, PolicyStatus policyStatus, Double premiumRate) {
        logger.info("Updating status for UserPolicy ID: {} to {} with premium: {}", id, policyStatus, premiumRate);

        UserPolicy userPolicy = userPolicyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("UserPolicy not found with ID: {}", id);
                    return new PolicyNotFoundException("UserPolicy not found with id: " + id);
                });

        userPolicy.setStatus(policyStatus);
        userPolicy.setPremiumPaid(premiumRate);

        UserPolicy updated = userPolicyRepository.save(userPolicy);
        logger.info("UserPolicy ID {} updated successfully", id);

        return mapToResponse(updated);
    }

    /**
     * Maps a UserPolicy entity to a UserPolicyResponse DTO.
     *
     * @param entity the UserPolicy entity
     * @return the mapped UserPolicyResponse
     */
    private UserPolicyResponse mapToResponse(UserPolicy entity) {
        return new UserPolicyResponse(
                entity.getId(),
                entity.getUser().getId(),
                entity.getPolicy().getId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getStatus().name(),
                entity.getPremiumPaid(),
                entity.getPolicy().getName(),
                entity.getPolicy().getType(),
                entity.getNoClaimBonus(),
                entity.getPolicy().getCoverageAmt()
        );
    }
}
