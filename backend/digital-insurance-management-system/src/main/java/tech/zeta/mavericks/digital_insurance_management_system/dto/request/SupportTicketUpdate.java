package tech.zeta.mavericks.digital_insurance_management_system.dto.request;

import lombok.Data;

/**
 * Data Transfer Object for updating an existing support ticket.
 *
 * This DTO is used to capture changes to a support ticket,
 * including related policy or claim, subject, description, and status.
 */
@Data
public class SupportTicketUpdate {

    /** Optional updated policy ID associated with the ticket */
    private Long policyId;

    /** Optional updated claim ID associated with the ticket */
    private Long claimId;

    /** Updated subject of the support ticket */
    private String subject;

    /** Updated detailed description of the ticket */
    private String description;

    /** Updated status of the ticket (e.g., OPEN, IN_PROGRESS, RESOLVED, CLOSED) */
    private String status;
}
