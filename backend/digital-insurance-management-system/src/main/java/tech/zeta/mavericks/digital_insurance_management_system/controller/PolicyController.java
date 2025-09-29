package tech.zeta.mavericks.digital_insurance_management_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.PolicyRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.PolicyWithPremium;
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
    public ResponseEntity<List<PolicyWithPremium>> getPoliciesForUser(@RequestBody PolicyRequest policyRequest) {
        List<PolicyWithPremium> policies = policyService.getPoliciesForUser(policyRequest);
        return ResponseEntity.ok(policies);
    }

    @PostMapping("/vehiclePolicies")
    public ResponseEntity<List<PolicyWithPremium>> getVehiclePoliciesForUser(@RequestBody PolicyRequest policyRequest) {
        List<PolicyWithPremium> policies = policyService.getVehiclePoliciesForUser(policyRequest);
        return ResponseEntity.ok(policies);
    }

    @PostMapping("/lifePolicies")
    public ResponseEntity<List<PolicyWithPremium>> getLifePoliciesForUser(@RequestBody PolicyRequest policyRequest) {
        List<PolicyWithPremium> policies = policyService.getLifePoliciesForUser(policyRequest);
        return ResponseEntity.ok(policies);
    }

    @PostMapping("/healthPolicies")
    public ResponseEntity<List<PolicyWithPremium>> getHealthPoliciesForUser(@RequestBody PolicyRequest policyRequest) {
        List<PolicyWithPremium> policies = policyService.getHealthPoliciesForUser(policyRequest);
        return ResponseEntity.ok(policies);
    }
}
