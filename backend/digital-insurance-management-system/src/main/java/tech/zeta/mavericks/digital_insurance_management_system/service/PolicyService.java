package tech.zeta.mavericks.digital_insurance_management_system.service;

import org.springframework.stereotype.Service;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.PolicyRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.PolicyWithPremium;
import tech.zeta.mavericks.digital_insurance_management_system.repository.PolicyRepository;

import java.util.List;

@Service
public class PolicyService {

    private final PolicyRepository.PolicyCustomRepository policyRepo;

    public PolicyService(PolicyRepository.PolicyCustomRepository policyRepo) {
        this.policyRepo = policyRepo;
    }

    public List<PolicyWithPremium> getPoliciesForUser(PolicyRequest policyRequest) {
        return policyRepo.findPoliciesForUser(
                policyRequest
        );
    }

    public List<PolicyWithPremium> getVehiclePoliciesForUser(PolicyRequest policyRequest){
        return policyRepo.findVehiclePoliciesForUser(
              policyRequest
        );
    }

    public List<PolicyWithPremium> getLifePoliciesForUser(PolicyRequest policyRequest){
        return policyRepo.findLifePoliciesForUser(
               policyRequest
        );
    }

    public List<PolicyWithPremium> getHealthPoliciesForUser(PolicyRequest policyRequest){
        return policyRepo.findHealthPoliciesForUser(
                policyRequest
        );
    }
}
