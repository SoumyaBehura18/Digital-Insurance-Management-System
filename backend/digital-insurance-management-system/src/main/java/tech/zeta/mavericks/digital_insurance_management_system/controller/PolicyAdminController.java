package tech.zeta.mavericks.digital_insurance_management_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.PolicyAdminResponse;
import tech.zeta.mavericks.digital_insurance_management_system.entity.*;
import tech.zeta.mavericks.digital_insurance_management_system.service.PolicyServiceAdmin;

import java.util.List;

@RestController
@RequestMapping("/api/admin/policies")
public class PolicyAdminController {

    private final PolicyServiceAdmin policyServiceAdmin;

    @GetMapping
    public ResponseEntity<List<PolicyAdminResponse>> getAllPolicies() {
        return ResponseEntity.ok(policyServiceAdmin.getAllPolicies());
    }


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
            @RequestBody tech.zeta.mavericks.digital_insurance_management_system.dto.premium.HealthPremiumRequest healthPremiumRequest) {
        return ResponseEntity.ok(policyServiceAdmin.addHealthPremium(
                policyId,
                healthPremiumRequest.getConditionPremiums(),   // expects Set<ConditionPremiumDTO>
                healthPremiumRequest.getPremiumRate(),
                healthPremiumRequest.getRenewalRate()
        ));
    }


    @PostMapping("/{policyId}/preexisting-condition")
    public ResponseEntity<?> addPreexistingCondition(
            @PathVariable Long policyId,
            @RequestBody tech.zeta.mavericks.digital_insurance_management_system.dto.premium.HealthPremiumRequest request) {
        return ResponseEntity.ok(
                policyServiceAdmin.addPreexistingCondition(
                        policyId,
                        request.getConditionPremiums()
                )
        );
    }


    @PostMapping("/{policyId}/life-premium")
    public ResponseEntity<Double> addLifePremium(
            @PathVariable Long policyId,
            @RequestBody tech.zeta.mavericks.digital_insurance_management_system.dto.premium.LifePremiumRequest request) {

        double total = policyServiceAdmin.addLifePremium(
                policyId,
                request.getPremiumRate(),
                request.getRenewalRate()
        );

        return ResponseEntity.ok(total);
    }

    // 4️⃣ Add Vehicle Policy Premium
    @PostMapping("/{policyId}/vehicle-premium")
    public ResponseEntity<Double> addVehiclePremium(
            @PathVariable Long policyId,
            @RequestBody tech.zeta.mavericks.digital_insurance_management_system.dto.premium.VehiclePremiumRequest request) {

        double total = policyServiceAdmin.addVehiclePremium(
                policyId,
                request.getPremiumRate(),
                request.getRenewalRate(),
                request.getVehicleAge()
        );

        return ResponseEntity.ok(total);
    }


}
