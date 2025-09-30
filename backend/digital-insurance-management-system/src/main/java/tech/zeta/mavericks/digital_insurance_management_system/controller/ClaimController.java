package tech.zeta.mavericks.digital_insurance_management_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.ClaimReview;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.ClaimListResponse;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.RemainingCoverageResponse;
import tech.zeta.mavericks.digital_insurance_management_system.entity.UserPolicy;
import tech.zeta.mavericks.digital_insurance_management_system.service.ClaimService;

import jakarta.validation.Valid;

import java.util.List;

/**
 * REST controller for managing insurance claims.
 * Handles claim submission, review, retrieval, and related operations.
 *
 * Endpoints:
 *  - POST /claim: Submit a new claim
 *  - GET /claim/claims: Get all claims (admin)
 *  - GET /claim/user/{userId}: Get claims by user
 *  - PUT /claim/{claimId}/review: Review a claim (admin)
 *  - GET /claim/policy/{policyId}: Get user policies by policy ID
 *  - GET /claim/policy/remaining-amount/{policyId}: Get remaining claimable amount
 *  - GET /claim/policies: Get all user policies
 *
 * @author Team Mavericks
 */
@RestController
@RequestMapping("/claim")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService claimService;

    /**
     * Submit a new insurance claim with supporting document.
     *
     * @param userPolicyId ID of the user's policy for the claim
     * @param claimAmount Amount being claimed (as string for decimal precision)
     * @param reason Reason/description for the claim
     * @param claimDate Date of incident (optional)
     * @param document Supporting document file
     * @return ResponseEntity with submitted claim details
     */
    @PostMapping
    public ResponseEntity<ClaimListResponse.ClaimResponseDto> submitClaim(
            @RequestParam("userPolicyId") Long userPolicyId,
            @RequestParam("claimAmount") String claimAmount,
            @RequestParam("reason") String reason,
            @RequestParam(value = "claimDate", required = false) String claimDate,
            @RequestParam("document") MultipartFile document) {
        
        ClaimListResponse.ClaimResponseDto response = claimService.submitClaimWithDocument(
                userPolicyId, claimAmount, reason, claimDate, document);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all claims in the system (admin access).
     *
     * @return List of all claims
     */
    @GetMapping("/claims")
    public ResponseEntity<List<ClaimListResponse>> getAllClaims() {
        List<ClaimListResponse> claims = claimService.getAllClaimsDto();
        return ResponseEntity.ok(claims);
    }

    /**
     * Get all claims submitted by a specific user.
     *
     * @param userId ID of the user
     * @return List of claims for the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ClaimListResponse>> getClaimsByUserId(@PathVariable Long userId) {
        List<ClaimListResponse> claims = claimService.getClaimsByUserIdDto(userId);
        return ResponseEntity.ok(claims);
    }

    /**
     * Review and update the status of a claim (admin only).
     *
     * @param claimReview Review data (status, comments)
     * @param claimId ID of the claim to review
     * @return Empty response on success
     */
    @PutMapping("/{claimId}/review")
    public ResponseEntity<Void> reviewClaim(@Valid @RequestBody ClaimReview claimReview, @PathVariable Long claimId) {
        claimService.updateCalimStatusAndReviewerComment(claimId, claimReview.getStatus(), claimReview.getReviewComments());
        return ResponseEntity.ok().build();
    }

    /**
     * Get user policies associated with a specific policy ID.
     *
     * @param policyId Policy ID
     * @return List of user policies
     */
    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<UserPolicy>> getPoliciesByPolicyId(@PathVariable Long policyId) {
        List<UserPolicy> policies = claimService.getUserPoliciesByUserId(policyId);
        return ResponseEntity.ok(policies);
    }

    /**
     * Get the remaining claimable amount for a policy.
     *
     * @param policyId Policy ID
     * @return RemainingCoverageResponse with available amount
     */
    @GetMapping("/policy/remaining-amount/{policyId}")
    public ResponseEntity<RemainingCoverageResponse> getRemainingClaimAmount(@PathVariable Long policyId) {
        RemainingCoverageResponse response = claimService.getRemainingCoverageAmount(policyId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all user policies in the system.
     *
     * @return List of all user policies
     */
    @GetMapping("/policies")
    public ResponseEntity<List<UserPolicy>> getAllPolicies() {
        List<UserPolicy> policies = claimService.getAllUserPolicies();
        return ResponseEntity.ok(policies);
    }
}
