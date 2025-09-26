package tech.zeta.mavericks.digital_insurance_management_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.PolicyWithPremiumDTO;
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

    @PostMapping("/allPolicies")
    public ResponseEntity<List<PolicyWithPremiumDTO>> getPoliciesForUser(@RequestBody User user) {
        List<PolicyWithPremiumDTO> policies = policyService.getPoliciesForUser(user);
        return ResponseEntity.ok(policies);
    }

    @PostMapping("/vehiclePolicies")
    public ResponseEntity<List<PolicyWithPremiumDTO>> getVehiclePoliciesForUser(@RequestBody User user) {
        List<PolicyWithPremiumDTO> policies = policyService.getVehiclePoliciesForUser(user);
        return ResponseEntity.ok(policies);
    }

    @PostMapping("/lifePolicies")
    public ResponseEntity<List<PolicyWithPremiumDTO>> getLifePoliciesForUser(@RequestBody User user) {
        List<PolicyWithPremiumDTO> policies = policyService.getLifePoliciesForUser(user);
        return ResponseEntity.ok(policies);
    }

    @PostMapping("/healthPolicies")
    public ResponseEntity<List<PolicyWithPremiumDTO>> getHealthPoliciesForUser(@RequestBody User user) {
        List<PolicyWithPremiumDTO> policies = policyService.getHealthPoliciesForUser(user);
        return ResponseEntity.ok(policies);
    }
}
