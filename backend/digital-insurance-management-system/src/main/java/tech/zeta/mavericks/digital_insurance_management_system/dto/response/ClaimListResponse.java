package tech.zeta.mavericks.digital_insurance_management_system.dto.response;

import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.ClaimStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO representing detailed information about an insurance claim.
 * Used for listing and returning claim data in API responses.
 *
 * @author Team Mavericks
 */
@Data
public class ClaimListResponse {
    /** Unique identifier for the claim */
    private Long id;
    /** ID of the user policy associated with this claim */
    private Long userPolicyId;
    /** ID of the user who submitted the claim */
    private Long userId;
    /** Name of the user who submitted the claim */
    private String userName;
    /** Email of the user who submitted the claim */
    private String userEmail;
    /** Name of the policy for which the claim was made */
    private String policyName;
    /** Date when the claim was filed */
    private LocalDate claimDate;
    /** Amount claimed by the user */
    private BigDecimal claimAmount;
    /** Reason or description for the claim */
    private String reason;
    /** Current status of the claim (PENDING, APPROVED, REJECTED) */
    private ClaimStatus status;
    /** Reviewer/admin comment for the claim */
    private String reviewerComment;
    /** Date when the claim was resolved */
    private LocalDate resolvedDate;
    /** Link to the uploaded document supporting the claim */
    private String documentLink;

    /**
     * Nested DTO for simplified claim response, e.g. after submission.
     * Contains only essential claim details.
     */
    @Data
    public static class ClaimResponseDto {
        /** Unique identifier for the claim */
        private Long id;
        /** ID of the user policy associated with this claim */
        private Long userPolicyId;
        /** Date when the claim was filed */
        private LocalDate claimDate;
        /** Amount claimed by the user */
        private BigDecimal claimAmount;
        /** Reason or description for the claim */
        private String reason;
        /** Current status of the claim (PENDING, APPROVED, REJECTED) */
        private ClaimStatus status;
        /** Reviewer/admin comment for the claim */
        private String reviewerComment;
        /** Date when the claim was resolved */
        private LocalDate resolvedDate;
        /** Link to the uploaded document supporting the claim */
        private String documentLink;
    }
}
