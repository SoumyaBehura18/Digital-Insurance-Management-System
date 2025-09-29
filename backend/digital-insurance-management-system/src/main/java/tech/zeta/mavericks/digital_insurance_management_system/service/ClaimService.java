package tech.zeta.mavericks.digital_insurance_management_system.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.ClaimRequestDto;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.ClaimListResponseDto;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.RemainingCoverageResponseDto;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Claim;
import tech.zeta.mavericks.digital_insurance_management_system.entity.UserPolicy;
import tech.zeta.mavericks.digital_insurance_management_system.enums.ClaimStatus;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyStatus;
import tech.zeta.mavericks.digital_insurance_management_system.exception.ClaimNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.exception.UserPolicyNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.repository.ClaimRepository;
import tech.zeta.mavericks.digital_insurance_management_system.repository.UserPolicyRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final UserPolicyRepository userPolicyRepository;

    public ClaimListResponseDto.ClaimResponseDto submitClaim(ClaimRequestDto claimRequestDto) {
        UserPolicy userPolicy = userPolicyRepository.findById(claimRequestDto.getUserPolicyId())
                .orElseThrow(() -> new UserPolicyNotFoundException("User policy with ID " + claimRequestDto.getUserPolicyId() + " not found"));

        log.info("UserPolicy status: {}", userPolicy.getStatus());
        log.info("UserPolicy start date: {}, end date: {}", userPolicy.getStartDate(), userPolicy.getEndDate());

        if (userPolicy.getStatus() == PolicyStatus.EXPIRED || userPolicy.getStatus() == PolicyStatus.CANCELLED || userPolicy.getStatus() == PolicyStatus.RENEW_PENDING) {
            throw new RuntimeException("Cannot submit claim for non-active policy (status=" + userPolicy.getStatus() + ")");
        }

        Claim claim = new Claim();
        claim.setUserPolicy(userPolicy);
        claim.setClaimDate(claimRequestDto.getClaimDate());
        claim.setClaimAmount(claimRequestDto.getClaimAmount());
        claim.setReason(claimRequestDto.getReason());
        claim.setStatus(ClaimStatus.PENDING);
        claim.setReviewerComment("");
        claim.setResolvedDate(null);

        Claim savedClaim = claimRepository.save(claim);
        log.info("Claim submitted successfully with ID: {}", savedClaim.getId());

        return convertToResponseDto(savedClaim);
    }

    private ClaimListResponseDto.ClaimResponseDto convertToResponseDto(Claim claim) {
        ClaimListResponseDto.ClaimResponseDto responseDto = new ClaimListResponseDto.ClaimResponseDto();
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

    public List<ClaimListResponseDto> getAllClaimsDto() {
        List<Claim> claims = claimRepository.findAll();
        return claims.stream()
                .map(this::convertToClaimListResponseDto)
                .collect(Collectors.toList());
    }

    public List<ClaimListResponseDto> getClaimsByUserIdDto(Long userId) {
        List<Claim> claims = claimRepository.findByUserPolicy_User_Id(userId);
        return claims.stream()
                .map(this::convertToClaimListResponseDto)
                .collect(Collectors.toList());
    }

    private ClaimListResponseDto convertToClaimListResponseDto(Claim claim) {
        ClaimListResponseDto dto = new ClaimListResponseDto();
        dto.setId(claim.getId());
        dto.setUserPolicyId(claim.getUserPolicy().getId());
        dto.setUserId(claim.getUserPolicy().getUser().getId());
        dto.setUserName(claim.getUserPolicy().getUser().getName());
        dto.setUserEmail(claim.getUserPolicy().getUser().getEmail());
        dto.setPolicyName(claim.getUserPolicy().getPolicy().getName());
        dto.setClaimDate(claim.getClaimDate());
        dto.setClaimAmount(claim.getClaimAmount());
        dto.setReason(claim.getReason());
        dto.setStatus(claim.getStatus());
        dto.setReviewerComment(claim.getReviewerComment());
        dto.setResolvedDate(claim.getResolvedDate());
        return dto;
    }

    public void updateCalimStatusAndReviewerComment(Long claimId, ClaimStatus status, String reviewerComment) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ClaimNotFoundException("Claim with ID " + claimId + " not found"));

        claim.setStatus(status);
        claim.setReviewerComment(reviewerComment);
        if (status == ClaimStatus.APPROVED || status == ClaimStatus.REJECTED) {
            claim.setResolvedDate(LocalDate.now());
        }
        claimRepository.save(claim);
    }

    public List<UserPolicy> getUserPoliciesByUserId(Long userId) {
        return userPolicyRepository.findByUser_Id(userId);
    }

    public RemainingCoverageResponseDto getRemainingCoverageAmount(Long userPolicyId) {
        UserPolicy userPolicy = userPolicyRepository.findById(userPolicyId)
                .orElseThrow(() -> new UserPolicyNotFoundException("User policy with ID " + userPolicyId + " not found"));

        Double totalClaimedAmount = claimRepository.findByUserPolicy_Id(userPolicyId).stream()
                .filter(claim -> claim.getStatus() == ClaimStatus.APPROVED)
                .mapToDouble(claim -> claim.getClaimAmount().doubleValue())
                .sum();

        Double coverageAmount = userPolicy.getPolicy().getCoverageAmt();
        Double remaining = coverageAmount - totalClaimedAmount;
        return new RemainingCoverageResponseDto(userPolicyId, remaining);
    }

    public List<UserPolicy> getAllUserPolicies() {
        return userPolicyRepository.findAll();
    }
}
