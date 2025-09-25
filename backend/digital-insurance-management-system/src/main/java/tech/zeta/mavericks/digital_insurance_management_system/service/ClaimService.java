package tech.zeta.mavericks.digital_insurance_management_system.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.zeta.mavericks.digital_insurance_management_system.dto.ClaimRequestDto;
import tech.zeta.mavericks.digital_insurance_management_system.dto.ClaimResponseDto;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Claim;
import tech.zeta.mavericks.digital_insurance_management_system.entity.UserPolicy;
import tech.zeta.mavericks.digital_insurance_management_system.enums.ClaimStatus;
import tech.zeta.mavericks.digital_insurance_management_system.exceptions.ClaimSubmissionException;
import tech.zeta.mavericks.digital_insurance_management_system.exceptions.UserPolicyNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.repository.ClaimRepository;
import tech.zeta.mavericks.digital_insurance_management_system.repository.UserPolicyRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final UserPolicyRepository userPolicyRepository;

    public ClaimResponseDto submitClaim(ClaimRequestDto claimRequestDto) {
        try {
            log.info("Submitting claim for user policy ID: {}", claimRequestDto.getUserPolicyId());

            // Validate if user policy exists and is active
            UserPolicy userPolicy = userPolicyRepository.findById(claimRequestDto.getUserPolicyId())
                    .orElseThrow(() -> new UserPolicyNotFoundException(
                            "User policy with ID " + claimRequestDto.getUserPolicyId() + " not found"));

            // Validate policy status (assuming you have status validation)
            validatePolicyForClaim(userPolicy);

            // Create new claim with default values
            Claim claim = new Claim();
            claim.setUserPolicy(userPolicy);
            claim.setClaimDate(claimRequestDto.getClaimDate());
            claim.setClaimAmount(claimRequestDto.getClaimAmount());
            claim.setReason(claimRequestDto.getReason());
            claim.setStatus(ClaimStatus.PENDING);
            claim.setReviewerComment("");
            claim.setResolvedDate(null);

            // Save claim
            Claim savedClaim = claimRepository.save(claim);
            log.info("Claim submitted successfully with ID: {}", savedClaim.getId());

            return convertToResponseDto(savedClaim);

        } catch (UserPolicyNotFoundException ex) {
            log.error("User policy not found: {}", ex.getMessage());
            throw ex;
        } catch (ClaimSubmissionException ex) {
            log.error("Claim submission error: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error during claim submission: {}", ex.getMessage(), ex);
            throw new ClaimSubmissionException("Failed to submit claim due to internal error");
        }
    }

    private void validatePolicyForClaim(UserPolicy userPolicy) {
        // Add your business logic validation here
        // Example: Check if policy is active, not expired, etc.
        if (!"ACTIVE".equals(userPolicy.getStatus())) {
            throw new ClaimSubmissionException("Cannot submit claim for inactive policy");
        }
    }

    private ClaimResponseDto convertToResponseDto(Claim claim) {
        ClaimResponseDto responseDto = new ClaimResponseDto();
        responseDto.setId(claim.getId());
        responseDto.setUserPolicyId(claim.getUserPolicy().getId());
        responseDto.setClaimDate(claim.getClaimDate());
        responseDto.setClaimAmount(claim.getClaimAmount());
        responseDto.setReason(claim.getReason());
        responseDto.setStatus(claim.getStatus());
        responseDto.setReviewerComment(claim.getReviewerComment());
        responseDto.setResolvedDate(claim.getResolvedDate());
        return responseDto;
    }
}
