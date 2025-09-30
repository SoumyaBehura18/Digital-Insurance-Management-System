package tech.zeta.mavericks.digital_insurance_management_system.dto.response;

import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.TicketStatus;

import java.sql.Timestamp;
import java.util.List;

/**
 * Data Transfer Object for returning support ticket details.
 *
 * This DTO is used to provide comprehensive information about a support ticket,
 * including related user, policy or claim IDs, subject, description, status, timestamps,
 * and associated messages.
 */
@Data
public class SupportTicketResponse {

    /** Unique ID of the support ticket */
    private Long id;

    /** ID of the user who submitted the ticket */
    private Long userId;

    /** Optional ID of the related policy */
    private Long policyId;

    /** Optional ID of the related claim */
    private Long claimId;

    /** Subject of the support ticket */
    private String subject;

    /** Detailed description of the support ticket */
    private String description;

    /** Current status of the ticket (e.g., OPEN, IN_PROGRESS, RESOLVED, CLOSED) */
    private TicketStatus status;

    /** Timestamp when the ticket was created */
    private Timestamp createdAt;

    /** Timestamp when the ticket was resolved */
    private Timestamp resolvedAt;

    /** List of messages associated with the support ticket */
    private List<MessageResponse> messages;
}
