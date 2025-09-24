package tech.zeta.mavericks.digital_insurance_management_system.DTO.response;

import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.model.TicketStatus;

import java.sql.Date;

@Data
public class SupportTicketResponseDTO {
    private Long id;
    private Long userId;
    private Long policyId;
    private Long claimId;
    private String subject;
    private String description;
    private TicketStatus status;
    private String response;
    private Date createdAt;
    private Date resolvedAt;
}

