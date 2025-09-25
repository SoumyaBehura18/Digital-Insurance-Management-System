package tech.zeta.mavericks.digital_insurance_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.UserPolicyRequest;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.UserPolicyResponse;
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
@Service
public class UserPolicyService {
    @Autowired private UserPolicyRepository userPolicyRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PolicyRepository policyRepository;

    public UserPolicyResponse saveUserPolicy(UserPolicyRequest request){
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Policy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found"));

        UserPolicy userPolicy = new UserPolicy();
        userPolicy.setUser(user);
        userPolicy.setPolicy(policy);
        userPolicy.setStartDate(request.getStartDate());
        userPolicy.setEndDate(request.getEndDate());
        userPolicy.setStatus(PolicyStatus.valueOf(request.getStatus()));
        userPolicy.setPremiumPaid(request.getPremiumPaid());

        UserPolicy saved = userPolicyRepository.save(userPolicy);

        return mapToResponse(saved);
    }

    public List<UserPolicyResponse> getUserPoliciesByUserId(Long userId) {
        List<UserPolicy> policies = userPolicyRepository.findByUserId(userId);
        if (policies.isEmpty()) {
            throw new PolicyNotFoundException("No policies found for user with ID " + userId);
        }
        return policies.stream()
                .map(this::mapToResponse)
                .toList();
    }


    public UserPolicyResponse getUserPolicyById(Long id){
        UserPolicy userPolicy = userPolicyRepository.findById(id).orElseThrow();
        return mapToResponse(userPolicy);
    }

    private UserPolicyResponse mapToResponse(UserPolicy entity) {
        return new UserPolicyResponse(
                entity.getId(),
                entity.getUser().getId(),
                entity.getPolicy().getId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getStatus().name(),
                entity.getPremiumPaid()
        );
    }
}
