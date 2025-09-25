package tech.zeta.mavericks.digital_insurance_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.zeta.mavericks.digital_insurance_management_system.entity.UserPolicy;
import tech.zeta.mavericks.digital_insurance_management_system.repository.UserPolicyRepository;

import java.util.List;

@Service
public class UserPolicyService {
    @Autowired private UserPolicyRepository policyRepository;

    public UserPolicy saveUserPolicy(UserPolicy userPolicy){
        return policyRepository.save(userPolicy);
    }

    public List<UserPolicy> getUserPolicies(){
        return policyRepository.findAll();
    }

    public UserPolicy getUserPolicyById(Long id){
        return policyRepository.findById(id).orElseThrow();
    }
}
