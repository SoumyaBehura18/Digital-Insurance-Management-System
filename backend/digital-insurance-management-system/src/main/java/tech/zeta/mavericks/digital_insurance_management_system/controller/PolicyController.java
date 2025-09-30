package tech.zeta.mavericks.digital_insurance_management_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.PolicyRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.PolicyWithPremium;
import tech.zeta.mavericks.digital_insurance_management_system.service.PolicyService;

import java.util.List;

/**
 * REST controller for managing user-accessible insurance policies.
 * Provides endpoints to retrieve available insurance policies by type
 * (all, vehicle, life, or health) based on user details or preferences.
 *
 * Endpoints:
 *  - POST /policies/allPolicies: Get all policies for a user
 *  - POST /policies/vehiclePolicies: Get vehicle insurance policies
 *  - POST /policies/lifePolicies: Get life insurance policies
 *  - POST /policies/healthPolicies: Get health insurance policies
 *
 * This controller allows users to explore different insurance policy options
 * along with calculated premium details.
 */
@RestController
@RequestMapping("/policies")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    /**
     * Retrieve all available policies for a given user.
     *
     * @param policyRequest Request containing user details or filters
     * @return List of all policies with premium details
     */
    @PostMapping("/allPolicies")
    public ResponseEntity<List<PolicyWithPremium>> getPoliciesForUser(@RequestBody PolicyRequest policyRequest) {
        List<PolicyWithPremium> policies = policyService.getPoliciesForUser(policyRequest);
        return ResponseEntity.ok(policies);
    }

    /**
     * Retrieve vehicle insurance policies available for a user.
     *
     * @param policyRequest Request containing user details or filters
     * @return List of vehicle policies with calculated premium details
     */
    @PostMapping("/vehiclePolicies")
    public ResponseEntity<List<PolicyWithPremium>> getVehiclePoliciesForUser(@RequestBody PolicyRequest policyRequest) {
        List<PolicyWithPremium> policies = policyService.getVehiclePoliciesForUser(policyRequest);
        return ResponseEntity.ok(policies);
    }

    /**
     * Retrieve life insurance policies available for a user.
     *
     * @param policyRequest Request containing user details or filters
     * @return List of life policies with calculated premium details
     */
    @PostMapping("/lifePolicies")
    public ResponseEntity<List<PolicyWithPremium>> getLifePoliciesForUser(@RequestBody PolicyRequest policyRequest) {
        List<PolicyWithPremium> policies = policyService.getLifePoliciesForUser(policyRequest);
        return ResponseEntity.ok(policies);
    }

    /**
     * Retrieve health insurance policies available for a user.
     *
     * @param policyRequest Request containing user details or filters
     * @return List of health policies with calculated premium details
     */
    @PostMapping("/healthPolicies")
    public ResponseEntity<List<PolicyWithPremium>> getHealthPoliciesForUser(@RequestBody PolicyRequest policyRequest) {
        List<PolicyWithPremium> policies = policyService.getHealthPoliciesForUser(policyRequest);
        return ResponseEntity.ok(policies);
    }
}
