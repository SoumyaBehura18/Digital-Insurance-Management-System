package tech.zeta.mavericks.digital_insurance_management_system.dto.request;

import lombok.Data;

/**
 * Data Transfer Object for creating a new support ticket.
 *
 * This DTO is used to capture details required to submit a support request,
 * including user information, related policy or claim, subject, and description.
 */
@Data
public class SupportTicketRequest {

    /** ID of the user submitting the support ticket */
    private Long userId;

    /** Optional ID of the related policy */
    private Long policyId;

    /** Optional ID of the related claim */
    private Long claimId;

    /** Subject of the support ticket */
    private String subject;

    /** Detailed description of the issue or request */
    private String description;
}
