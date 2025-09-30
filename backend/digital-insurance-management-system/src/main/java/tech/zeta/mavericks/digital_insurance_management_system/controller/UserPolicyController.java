package tech.zeta.mavericks.digital_insurance_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.PolicyStatusRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.UserPolicyRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.UserPolicyResponse;
import tech.zeta.mavericks.digital_insurance_management_system.service.UserPolicyService;

import java.util.List;

/**
 * REST controller for managing user insurance policies.
 * Provides endpoints for purchasing, retrieving, and updating user policies.
 *
 * Endpoints:
 *  - POST /user/policy/purchase: Purchase a new policy for a user
 *  - GET /user/policies/{userId}: Retrieve all policies of a specific user
 *  - GET /user/policy/{id}: Retrieve a specific user policy by ID
 *  - PATCH /user/policy/ncb/{id}: Update No Claim Bonus (NCB) for a policy
 *  - PATCH /user/policy/status/{id}: Update the status or premium of a policy
 */
@RestController
@RequestMapping("/user")
public class UserPolicyController {

    @Autowired
    public UserPolicyService userPolicyService;

    /**
     * Purchase a new insurance policy for a user.
     *
     * @param request Request containing policy and user details
     * @return Created user policy details
     */
    @PostMapping("/policy/purchase")
    public ResponseEntity<UserPolicyResponse> saveUserPolicy(@RequestBody UserPolicyRequest request) {
        return new ResponseEntity<>(userPolicyService.saveUserPolicy(request), HttpStatus.CREATED);
    }

    /**
     * Retrieve all policies associated with a specific user.
     *
     * @param userId ID of the user
     * @return List of user policies
     */
    @GetMapping("/policies/{userId}")
    public ResponseEntity<List<UserPolicyResponse>> getUserPoliciesByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(userPolicyService.getUserPoliciesByUserId(userId), HttpStatus.OK);
    }

    /**
     * Retrieve a specific user policy by its ID.
     *
     * @param id User policy ID
     * @return User policy details
     */
    @GetMapping("/policy/{id}")
    public ResponseEntity<UserPolicyResponse> getUserPolicyById(@PathVariable Long id) {
        return new ResponseEntity<>(userPolicyService.getUserPolicyById(id), HttpStatus.OK);
    }

    /**
     * Update the No Claim Bonus (NCB) for a specific user policy.
     *
     * @param id User policy ID
     * @return Updated user policy with modified NCB
     */
    @PatchMapping("/policy/ncb/{id}")
    public ResponseEntity<UserPolicyResponse> updateUserPolicyById(@PathVariable Long id) {
        return new ResponseEntity<>(userPolicyService.updateUserPolicyById(id), HttpStatus.OK);
    }

    /**
     * Update the status or premium rate of a user policy.
     *
     * @param id User policy ID
     * @param request Request containing new policy status and premium rate
     * @return Updated user policy details
     */
    @PatchMapping("/policy/status/{id}")
    public ResponseEntity<UserPolicyResponse> updateUserPolicyStatusById(
            @PathVariable Long id,
            @RequestBody PolicyStatusRequest request) {
        return new ResponseEntity<>(
                userPolicyService.updateUserPolicyStatusById(id, request.getPolicyStatus(), request.getPremiumRate()),
                HttpStatus.OK
        );
    }
}
