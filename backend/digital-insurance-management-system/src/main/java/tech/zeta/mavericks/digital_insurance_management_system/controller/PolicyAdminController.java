package tech.zeta.mavericks.digital_insurance_management_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.PolicyAdminResponse;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Policy;
import tech.zeta.mavericks.digital_insurance_management_system.service.PolicyServiceAdmin;

import java.util.List;

/**
 * REST controller for managing policies from the admin side.
 * Provides endpoints for creating and configuring insurance policies,
 * including premium management for health, life, and vehicle policies.
 *
 * Endpoints:
 *  - GET /api/admin/policies: Get all policies
 *  - POST /api/admin/policies: Create a new base policy
 *  - POST /api/admin/policies/{policyId}/health-premium: Add health policy premium
 *  - POST /api/admin/policies/{policyId}/preexisting-condition: Add preexisting health condition premiums
 *  - POST /api/admin/policies/{policyId}/life-premium: Add life policy premium
 *  - POST /api/admin/policies/{policyId}/vehicle-premium: Add vehicle policy premium

 */
@RestController
@RequestMapping("/api/admin/policies")
public class PolicyAdminController {

    private final PolicyServiceAdmin policyServiceAdmin;

    public PolicyAdminController(PolicyServiceAdmin policyServiceAdmin) {
        this.policyServiceAdmin = policyServiceAdmin;
    }

    /**
     * Retrieve all available policies for administrative management.
     *
     * @return List of policies with details
     */
    @GetMapping
    public ResponseEntity<List<PolicyAdminResponse>> getAllPolicies() {
        return ResponseEntity.ok(policyServiceAdmin.getAllPolicies());
    }

    /**
     * Create a new base insurance policy.
     *
     * @param policy Policy entity containing policy details
     * @return Created policy with assigned ID
     */
    @PostMapping
    public ResponseEntity<Policy> createPolicy(@RequestBody Policy policy) {
        return ResponseEntity.ok(policyServiceAdmin.createPolicy(policy));
    }

    /**
     * Add premium details for a health insurance policy.
     *
     * @param policyId ID of the policy
     * @param healthPremiumRequest Request containing premium rates and condition-based premiums
     * @return Updated health policy premium information
     */
    @PostMapping("/{policyId}/health-premium")
    public ResponseEntity<?> addHealthPremium(
            @PathVariable Long policyId,
            @RequestBody tech.zeta.mavericks.digital_insurance_management_system.dto.premium.HealthPremiumRequest healthPremiumRequest) {
        return ResponseEntity.ok(policyServiceAdmin.addHealthPremium(
                policyId,
                healthPremiumRequest.getConditionPremiums(),
                healthPremiumRequest.getPremiumRate(),
                healthPremiumRequest.getRenewalRate()
        ));
    }

    /**
     * Add preexisting health condition premium details to a policy.
     *
     * @param policyId ID of the policy
     * @param request Request containing condition premiums
     * @return Updated policy with added preexisting condition premiums
     */
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

    /**
     * Add premium rates for a life insurance policy.
     *
     * @param policyId ID of the policy
     * @param request Request containing premium and renewal rates
     * @return Total calculated life policy premium
     */
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

    /**
     * Add premium rates for a vehicle insurance policy.
     *
     * @param policyId ID of the policy
     * @param request Request containing premium details and vehicle age
     * @return Total calculated vehicle policy premium
     */
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
