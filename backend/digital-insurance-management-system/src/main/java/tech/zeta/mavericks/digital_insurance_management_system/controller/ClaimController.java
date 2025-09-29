package tech.zeta.mavericks.digital_insurance_management_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.ClaimReview;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.ClaimRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.ClaimListResponse;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.RemainingCoverageResponse;
import tech.zeta.mavericks.digital_insurance_management_system.entity.UserPolicy;
import tech.zeta.mavericks.digital_insurance_management_system.service.ClaimService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/claim")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService claimService;

    @PostMapping
    public ResponseEntity<ClaimListResponse.ClaimResponseDto> submitClaim(@Valid @RequestBody ClaimRequest claimRequest) {
        ClaimListResponse.ClaimResponseDto response = claimService.submitClaim(claimRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/claims")
    public ResponseEntity<List<ClaimListResponse>> getAllClaims() {
        List<ClaimListResponse> claims = claimService.getAllClaimsDto();
        return ResponseEntity.ok(claims);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ClaimListResponse>> getClaimsByUserId(@PathVariable Long userId) {
        List<ClaimListResponse> claims = claimService.getClaimsByUserIdDto(userId);
        return ResponseEntity.ok(claims);
    }

    @PutMapping("/{claimId}/review")
    public ResponseEntity<Void> reviewClaim(@Valid @RequestBody ClaimReview claimReview, @PathVariable Long claimId) {
        claimService.updateCalimStatusAndReviewerComment(claimId, claimReview.getStatus(), claimReview.getReviewComments());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<UserPolicy>> getPoliciesByPolicyId(@PathVariable Long policyId) {
        List<UserPolicy> policies = claimService.getUserPoliciesByUserId(policyId);
        return ResponseEntity.ok(policies);
    }

    @GetMapping("/policy/remaining-amount/{policyId}")
    public ResponseEntity<RemainingCoverageResponse> getRemainingClaimAmount(@PathVariable Long policyId) {
        RemainingCoverageResponse response = claimService.getRemainingCoverageAmount(policyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/policies")
    public ResponseEntity<List<UserPolicy>> getAllPolicies() {
        List<UserPolicy> policies = claimService.getAllUserPolicies();
        return ResponseEntity.ok(policies);
    }
}
