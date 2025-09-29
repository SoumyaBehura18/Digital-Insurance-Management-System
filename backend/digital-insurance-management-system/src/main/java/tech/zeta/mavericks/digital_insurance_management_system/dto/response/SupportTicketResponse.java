package tech.zeta.mavericks.digital_insurance_management_system.dto.response;

import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.TicketStatus;


import java.sql.Timestamp;
import java.util.List;

@Data
public class SupportTicketResponse {
    private Long id;
    private Long userId;
    private Long policyId;
    private Long claimId;
    private String subject;
    private String description;
    private TicketStatus status;
    private Timestamp createdAt;
    private Timestamp resolvedAt;

    private List<MessageResponse> messages;
}

