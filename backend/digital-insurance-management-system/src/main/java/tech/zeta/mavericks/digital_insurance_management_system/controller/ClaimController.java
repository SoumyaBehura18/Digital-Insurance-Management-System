package tech.zeta.mavericks.digital_insurance_management_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.ClaimReviewDTO;
import tech.zeta.mavericks.digital_insurance_management_system.dto.ClaimRequestDto;
import tech.zeta.mavericks.digital_insurance_management_system.dto.ClaimResponseDto;
import tech.zeta.mavericks.digital_insurance_management_system.dto.ClaimListResponseDto;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Claim;
import tech.zeta.mavericks.digital_insurance_management_system.entity.UserPolicy;
import tech.zeta.mavericks.digital_insurance_management_system.service.ClaimService;
import tech.zeta.mavericks.digital_insurance_management_system.enums.ClaimStatus;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/claim")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService claimService;

    @PostMapping
    public ResponseEntity<ClaimResponseDto> submitClaim(@Valid @RequestBody ClaimRequestDto claimRequestDto) {
        ClaimResponseDto response = claimService.submitClaim(claimRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/claims")
    public ResponseEntity<List<ClaimListResponseDto>> getAllClaims() {
        List<ClaimListResponseDto> response = claimService.getAllClaimsDto();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ClaimListResponseDto>> getClaimsByUserId(@PathVariable Long userId) {
        List<ClaimListResponseDto> response = claimService.getClaimsByUserIdDto(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{claimId}/review")
    public ResponseEntity<?> reviewClaim(@Valid @RequestBody ClaimReviewDTO claimReviewDTO, @PathVariable Long claimId) {
        claimService.updateCalimStatusAndReviewerComment(claimId, claimReviewDTO.getStatus(), claimReviewDTO.getReviewComments());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<?> getPoliciesByPolicyId(@PathVariable Long policyId) {
        List<UserPolicy> response = claimService.getUserPoliciesByUserId(policyId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/policy/remaining-amount/{policyId}")
    public ResponseEntity<?> getRemainingClaimAmount(@PathVariable Long policyId) {
        Double remainingAmount = claimService.getRemainingCoverageAmount(policyId);
        Map<String, Object> response = new HashMap<>();
        response.put("policyId", policyId);
        response.put("remainingClaimAmount", remainingAmount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/policies")
    public ResponseEntity<?> getAllPolicies() {
        List<UserPolicy> response = claimService.getAllUserPolicies();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
