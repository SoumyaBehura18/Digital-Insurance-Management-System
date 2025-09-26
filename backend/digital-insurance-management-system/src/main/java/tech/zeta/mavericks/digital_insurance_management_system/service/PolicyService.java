package tech.zeta.mavericks.digital_insurance_management_system.service;

import org.springframework.stereotype.Service;
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

    public List<PolicyWithPremiumDTO> getPoliciesForUser(User user) {
        System.out.println(user.toString());
        return policyRepo.findPoliciesForUser(
                user.getSmokingDrinking(),
                user.getVehicleType(),
                user.getVehicleAge(),
                user.getPreexistingConditions()
        );
    }

    public List<PolicyWithPremiumDTO> getVehiclePoliciesForUser(User user){
        return policyRepo.findVehiclePoliciesForUser(
                user.getSmokingDrinking(),
                user.getVehicleType(),
                user.getVehicleAge(),
                user.getPreexistingConditions()
        );
    }

    public List<PolicyWithPremiumDTO> getLifePoliciesForUser(User user){
        return policyRepo.findLifePoliciesForUser(
                user.getSmokingDrinking(),
                user.getVehicleType(),
                user.getVehicleAge(),
                user.getPreexistingConditions()
        );
    }

    public List<PolicyWithPremiumDTO> getHealthPoliciesForUser(User user){
        return policyRepo.findHealthPoliciesForUser(
                user.getSmokingDrinking(),
                user.getVehicleType(),
                user.getVehicleAge(),
                user.getPreexistingConditions()
        );
    }
}
