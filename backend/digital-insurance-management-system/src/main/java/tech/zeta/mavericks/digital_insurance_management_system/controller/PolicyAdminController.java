package tech.zeta.mavericks.digital_insurance_management_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.zeta.mavericks.digital_insurance_management_system.dto.HealthPolicyPremiumDTO.HealthPremiumRequestDTO;
import tech.zeta.mavericks.digital_insurance_management_system.entity.*;
import tech.zeta.mavericks.digital_insurance_management_system.service.PolicyServiceAdmin;

@RestController
@RequestMapping("/api/admin/policies")
public class PolicyAdminController {

    private final PolicyServiceAdmin policyServiceAdmin;

    public PolicyAdminController(PolicyServiceAdmin policyServiceAdmin) {
        this.policyServiceAdmin = policyServiceAdmin;
    }

    // 1️⃣ Create a base policy
    @PostMapping
    public ResponseEntity<Policy> createPolicy(@RequestBody Policy policy) {
        return ResponseEntity.ok(policyServiceAdmin.createPolicy(policy));
    }

    // 2️⃣ Add Health Policy Premium
    @PostMapping("/{policyId}/health-premium")
    public ResponseEntity<?> addHealthPremium(
            @PathVariable Long policyId,
            @RequestBody HealthPremiumRequestDTO premium) {
        return ResponseEntity.ok(policyServiceAdmin.addHealthPremium(policyId,premium.getConditions(),premium.getSmoking()));
    }

    // 3️⃣ Add Life Policy Premium
    @PostMapping("/{policyId}/life-premium")
    public ResponseEntity<LifePolicyPremium> addLifePremium(
            @PathVariable Long policyId,
            @RequestBody LifePolicyPremium premium) {
        return ResponseEntity.ok(policyServiceAdmin.addLifePremium(policyId, premium));
    }

    // 4️⃣ Add Vehicle Policy Premium
    @PostMapping("/{policyId}/vehicle-premium")
    public ResponseEntity<VehiclePolicyPremium> addVehiclePremium(
            @PathVariable Long policyId,
            @RequestBody VehiclePolicyPremium premium) {
        return ResponseEntity.ok(policyServiceAdmin.addVehiclePremium(policyId, premium));
    }
}
