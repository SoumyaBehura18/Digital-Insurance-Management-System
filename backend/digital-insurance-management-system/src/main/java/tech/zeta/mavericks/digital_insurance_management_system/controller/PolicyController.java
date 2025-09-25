package tech.zeta.mavericks.digital_insurance_management_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.zeta.mavericks.digital_insurance_management_system.dto.PolicyWithPremiumDTO;
import tech.zeta.mavericks.digital_insurance_management_system.entity.User;
import tech.zeta.mavericks.digital_insurance_management_system.service.PolicyService;

import java.util.List;

@RestController
@RequestMapping("/policies")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping("/user")
    public ResponseEntity<List<PolicyWithPremiumDTO>> getPoliciesForUser(@RequestBody User user) {
        List<PolicyWithPremiumDTO> policies = policyService.getPoliciesForUser(user);
        return ResponseEntity.ok(policies);
    }
}
