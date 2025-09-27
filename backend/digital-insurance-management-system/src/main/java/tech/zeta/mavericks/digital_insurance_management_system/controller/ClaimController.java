package tech.zeta.mavericks.digital_insurance_management_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.ClaimReviewDTO;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.ClaimRequestDto;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.ClaimListResponseDto;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.RemainingCoverageResponseDto;
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
    public ResponseEntity<ClaimListResponseDto.ClaimResponseDto> submitClaim(@Valid @RequestBody ClaimRequestDto claimRequestDto) {
        ClaimListResponseDto.ClaimResponseDto response = claimService.submitClaim(claimRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/claims")
    public ResponseEntity<List<ClaimListResponseDto>> getAllClaims() {
        List<ClaimListResponseDto> claims = claimService.getAllClaimsDto();
        return ResponseEntity.ok(claims);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ClaimListResponseDto>> getClaimsByUserId(@PathVariable Long userId) {
        List<ClaimListResponseDto> claims = claimService.getClaimsByUserIdDto(userId);
        return ResponseEntity.ok(claims);
    }

    @PutMapping("/{claimId}/review")
    public ResponseEntity<Void> reviewClaim(@Valid @RequestBody ClaimReviewDTO claimReviewDTO, @PathVariable Long claimId) {
        claimService.updateCalimStatusAndReviewerComment(claimId, claimReviewDTO.getStatus(), claimReviewDTO.getReviewComments());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<UserPolicy>> getPoliciesByPolicyId(@PathVariable Long policyId) {
        List<UserPolicy> policies = claimService.getUserPoliciesByUserId(policyId);
        return ResponseEntity.ok(policies);
    }

    @GetMapping("/policy/remaining-amount/{policyId}")
    public ResponseEntity<RemainingCoverageResponseDto> getRemainingClaimAmount(@PathVariable Long policyId) {
        RemainingCoverageResponseDto response = claimService.getRemainingCoverageAmount(policyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/policies")
    public ResponseEntity<List<UserPolicy>> getAllPolicies() {
        List<UserPolicy> policies = claimService.getAllUserPolicies();
        return ResponseEntity.ok(policies);
    }
}
