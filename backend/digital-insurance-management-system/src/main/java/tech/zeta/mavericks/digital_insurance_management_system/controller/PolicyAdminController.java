package tech.zeta.mavericks.digital_insurance_management_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.premium.HealthPremiumRequestDTO;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.premium.LifePremiumRequestDTO;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.premium.VehiclePremiumRequestDTO;
import tech.zeta.mavericks.digital_insurance_management_system.entity.*;
import tech.zeta.mavericks.digital_insurance_management_system.service.PolicyServiceAdmin;

import java.util.Set;

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
            @RequestBody HealthPremiumRequestDTO healthPremiumRequestDTO) {
        return ResponseEntity.ok(policyServiceAdmin.addHealthPremium(
                policyId,
                healthPremiumRequestDTO.getConditionPremiums(),   // expects Set<ConditionPremiumDTO>
                healthPremiumRequestDTO.getPremiumRate(),
                healthPremiumRequestDTO.getRenewalRate()
        ));
    }


    @PostMapping("/{policyId}/preexisting-condition")
    public ResponseEntity<?> addPreexistingCondition(
            @PathVariable Long policyId,
            @RequestBody HealthPremiumRequestDTO request) {
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
            @RequestBody LifePremiumRequestDTO request) {

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
            @RequestBody VehiclePremiumRequestDTO request) {

        double total = policyServiceAdmin.addVehiclePremium(
                policyId,
                request.getPremiumRate(),
                request.getRenewalRate()
        );

        return ResponseEntity.ok(total);
    }


}
