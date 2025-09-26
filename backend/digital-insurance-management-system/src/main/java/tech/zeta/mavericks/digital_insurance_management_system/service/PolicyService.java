package tech.zeta.mavericks.digital_insurance_management_system.service;

import org.springframework.stereotype.Service;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.PolicyRequest;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.PolicyWithPremiumDTO;
import tech.zeta.mavericks.digital_insurance_management_system.entity.User;
import tech.zeta.mavericks.digital_insurance_management_system.repository.PolicyRepository;

import java.util.List;

@Service
public class PolicyService {

    private final PolicyRepository.PolicyCustomRepository policyRepo;

    public PolicyService(PolicyRepository.PolicyCustomRepository policyRepo) {
        this.policyRepo = policyRepo;
    }

    public List<PolicyWithPremiumDTO> getPoliciesForUser(PolicyRequest policyRequest) {
        return policyRepo.findPoliciesForUser(
                policyRequest
        );
    }

    public List<PolicyWithPremiumDTO> getVehiclePoliciesForUser(PolicyRequest policyRequest){
        return policyRepo.findVehiclePoliciesForUser(
              policyRequest
        );
    }

    public List<PolicyWithPremiumDTO> getLifePoliciesForUser(PolicyRequest policyRequest){
        return policyRepo.findLifePoliciesForUser(
               policyRequest
        );
    }

    public List<PolicyWithPremiumDTO> getHealthPoliciesForUser(PolicyRequest policyRequest){
        return policyRepo.findHealthPoliciesForUser(
                policyRequest
        );
    }
}
