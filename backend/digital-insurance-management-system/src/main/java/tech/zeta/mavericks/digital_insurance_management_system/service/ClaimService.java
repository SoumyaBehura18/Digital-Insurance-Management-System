package tech.zeta.mavericks.digital_insurance_management_system.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.ClaimRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.ClaimListResponse;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.RemainingCoverageResponse;
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

/**
 * Service class for managing insurance claims business logic.
 * Handles claim submission, validation, document upload, review processing,
 * and coverage calculations.
 *
 * This service coordinates between repositories and external services
 * to provide complete claim management functionality.
 *
 * @author Team Mavericks
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final UserPolicyRepository userPolicyRepository;
    private final SupabaseStorageService storageService;

    /**
     * Submit a new claim with mandatory document validation and upload
     * 
     * This method handles the complete business logic for claim submission:
     * 1. Validates that document is provided and not empty
     * 2. Creates ClaimRequest from provided parameters
     * 3. Validates the user policy exists and is active
     * 4. Submits the claim to database
     * 5. Uploads the document to Supabase storage
     * 6. Updates the claim with document URL
     * 
     * @param userPolicyId - ID of the user's policy to claim against
     * @param claimAmount - Amount being claimed (as String)
     * @param reason - Detailed reason/description for the claim
     * @param claimDate - Date of the incident (optional)
     * @param document - MANDATORY supporting document file
     * @return ClaimResponseDto with claim details including document link
     * @throws RuntimeException if document is missing or upload fails
     */
    public ClaimListResponse.ClaimResponseDto submitClaimWithDocument(
            Long userPolicyId, String claimAmount, String reason, String claimDate, MultipartFile document) {
        
        // MANDATORY DOCUMENT VALIDATION
        if (document == null || document.isEmpty()) {
            log.error("Document is mandatory but not provided for claim submission");
            throw new RuntimeException("Document is mandatory for claim submission");
        }
        
        // Create ClaimRequest object with all required data
        ClaimRequest claimRequest = new ClaimRequest();
        claimRequest.setUserPolicyId(userPolicyId);
        claimRequest.setClaimAmount(new java.math.BigDecimal(claimAmount));
        claimRequest.setReason(reason);
        
        // Set claim date - use provided date or current date as fallback
        if (claimDate != null && !claimDate.isEmpty()) {
            claimRequest.setClaimDate(java.time.LocalDate.parse(claimDate));
        } else {
            claimRequest.setClaimDate(java.time.LocalDate.now());
        }
        
        // Submit claim to database first using existing method
        ClaimListResponse.ClaimResponseDto response = submitClaim(claimRequest);
        
        // MANDATORY DOCUMENT UPLOAD
        try {
            log.info("Uploading mandatory document: {} for claim ID: {}", 
                    document.getOriginalFilename(), response.getId());
            log.info("File size: {} bytes, Content type: {}", 
                    document.getSize(), document.getContentType());
            
            // Upload to Supabase storage
            String documentUrl = storageService.uploadFile(document, response.getId());
            log.info("Document uploaded successfully: {}", documentUrl);
            
            // Update the claim record with the document URL
            updateClaimDocument(response.getId(), documentUrl);
            response.setDocumentLink(documentUrl);
            
        } catch (Exception uploadException) {
            // Since document is mandatory, we should handle this error appropriately
            log.error("CRITICAL: Failed to upload mandatory document for claim {}", response.getId(), uploadException);
            
            // In production, consider deleting the claim if document upload fails
            // For now, we'll throw an exception to indicate the failure
            throw new RuntimeException("Failed to upload mandatory document: " + uploadException.getMessage());
        }
        
        return response;
    }

    /**
     * Submits a new insurance claim after validating policy eligibility.
     * Validates that the user policy exists and is in active status before
     * creating the claim record.
     *
     * @param claimRequest Request object containing claim details
     * @return ClaimResponseDto with submitted claim information
     * @throws UserPolicyNotFoundException if user policy not found
     * @throws RuntimeException if policy is not in active status
     */
    public ClaimListResponse.ClaimResponseDto submitClaim(ClaimRequest claimRequest) {
        UserPolicy userPolicy = userPolicyRepository.findById(claimRequest.getUserPolicyId())
                .orElseThrow(() -> new UserPolicyNotFoundException("User policy with ID " + claimRequest.getUserPolicyId() + " not found"));

        log.info("UserPolicy status: {}", userPolicy.getStatus());
        log.info("UserPolicy start date: {}, end date: {}", userPolicy.getStartDate(), userPolicy.getEndDate());

        if (userPolicy.getStatus() == PolicyStatus.EXPIRED || userPolicy.getStatus() == PolicyStatus.CANCELLED || userPolicy.getStatus() == PolicyStatus.RENEW_PENDING) {
            throw new RuntimeException("Cannot submit claim for non-active policy (status=" + userPolicy.getStatus() + ")");
        }

        Claim claim = new Claim();
        claim.setUserPolicy(userPolicy);
        claim.setClaimDate(claimRequest.getClaimDate());
        claim.setClaimAmount(claimRequest.getClaimAmount());
        claim.setReason(claimRequest.getReason());
        claim.setStatus(ClaimStatus.PENDING);
        claim.setReviewerComment("");
        claim.setResolvedDate(null);

        Claim savedClaim = claimRepository.save(claim);
        log.info("Claim submitted successfully with ID: {}", savedClaim.getId());

        return convertToResponseDto(savedClaim);
    }

    /**
     * Converts a Claim entity to ClaimResponseDto for API responses.
     * Maps all claim fields to the DTO structure.
     *
     * @param claim Claim entity to convert
     * @return ClaimResponseDto with claim data
     */
    private ClaimListResponse.ClaimResponseDto convertToResponseDto(Claim claim) {
        ClaimListResponse.ClaimResponseDto responseDto = new ClaimListResponse.ClaimResponseDto();
        responseDto.setId(claim.getId());
        responseDto.setUserPolicyId(claim.getUserPolicy().getId());
        responseDto.setClaimDate(claim.getClaimDate());
        responseDto.setClaimAmount(claim.getClaimAmount());
        responseDto.setReason(claim.getReason());
        responseDto.setStatus(claim.getStatus());
        responseDto.setReviewerComment(claim.getReviewerComment());
        responseDto.setResolvedDate(claim.getResolvedDate());
        responseDto.setDocumentLink(claim.getDocumentLink());
        return responseDto;
    }

    /**
     * Retrieves all claims in the system with complete user and policy details.
     * Typically used by administrators for claim management.
     *
     * @return List of ClaimListResponse DTOs with full claim information
     */
    public List<ClaimListResponse> getAllClaimsDto() {
        List<Claim> claims = claimRepository.findAll();
        return claims.stream()
                .map(this::convertToClaimListResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all claims submitted by a specific user.
     * Returns claims with full details including user and policy information.
     *
     * @param userId ID of the user whose claims to retrieve
     * @return List of ClaimListResponse DTOs for the specified user
     */
    public List<ClaimListResponse> getClaimsByUserIdDto(Long userId) {
        List<Claim> claims = claimRepository.findByUserPolicy_User_Id(userId);
        return claims.stream()
                .map(this::convertToClaimListResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Converts a Claim entity to ClaimListResponse DTO with complete information.
     * Includes user details, policy information, and claim data for list views.
     *
     * @param claim Claim entity to convert
     * @return ClaimListResponse DTO with full claim details
     */
    private ClaimListResponse convertToClaimListResponseDto(Claim claim) {
        ClaimListResponse dto = new ClaimListResponse();
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
        dto.setDocumentLink(claim.getDocumentLink());
        return dto;
    }

    /**
     * Updates the status and reviewer comments for a claim.
     * Sets the resolved date when claim is approved or rejected.
     *
     * @param claimId ID of the claim to update
     * @param status New claim status (PENDING, APPROVED, REJECTED)
     * @param reviewerComment Comments from the reviewer
     * @throws ClaimNotFoundException if claim with given ID is not found
     */
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

    /**
     * Retrieves all user policies for a specific user.
     *
     * @param userId ID of the user
     * @return List of UserPolicy entities belonging to the user
     */
    public List<UserPolicy> getUserPoliciesByUserId(Long userId) {
        return userPolicyRepository.findByUser_Id(userId);
    }

    /**
     * Calculates the remaining claimable amount for a user policy.
     * Subtracts total approved claims from the policy coverage amount.
     *
     * @param userPolicyId ID of the user policy
     * @return RemainingCoverageResponse with policy ID and remaining amount
     * @throws UserPolicyNotFoundException if user policy not found
     */
    public RemainingCoverageResponse getRemainingCoverageAmount(Long userPolicyId) {
        UserPolicy userPolicy = userPolicyRepository.findById(userPolicyId)
                .orElseThrow(() -> new UserPolicyNotFoundException("User policy with ID " + userPolicyId + " not found"));

        Double totalClaimedAmount = claimRepository.findByUserPolicy_Id(userPolicyId).stream()
                .filter(claim -> claim.getStatus() == ClaimStatus.APPROVED)
                .mapToDouble(claim -> claim.getClaimAmount().doubleValue())
                .sum();

        Double coverageAmount = userPolicy.getPolicy().getCoverageAmt();
        Double remaining = coverageAmount - totalClaimedAmount;
        return new RemainingCoverageResponse(userPolicyId, remaining);
    }

    /**
     * Retrieves all user policies in the system.
     *
     * @return List of all UserPolicy entities
     */
    public List<UserPolicy> getAllUserPolicies() {
        return userPolicyRepository.findAll();
    }

    /**
     * Updates the document link for an existing claim.
     * Used after successful document upload to Supabase storage.
     *
     * @param claimId ID of the claim to update
     * @param documentUrl URL of the uploaded document
     * @throws ClaimNotFoundException if claim with given ID is not found
     */
    public void updateClaimDocument(Long claimId, String documentUrl) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ClaimNotFoundException("Claim with ID " + claimId + " not found"));
        claim.setDocumentLink(documentUrl);
        claimRepository.save(claim);
    }
}
