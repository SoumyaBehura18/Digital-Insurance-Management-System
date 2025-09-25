package tech.zeta.mavericks.digital_insurance_management_system.service;

import org.springframework.stereotype.Service;
import tech.zeta.mavericks.digital_insurance_management_system.dto.PolicyWithPremiumDTO;
import tech.zeta.mavericks.digital_insurance_management_system.entity.User;
import tech.zeta.mavericks.digital_insurance_management_system.repository.interfaces.PolicyCustomRepository;

import java.util.List;

@Service
public class PolicyService {

    private final PolicyCustomRepository policyRepo;

    public PolicyService(PolicyCustomRepository policyRepo) {
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
}
