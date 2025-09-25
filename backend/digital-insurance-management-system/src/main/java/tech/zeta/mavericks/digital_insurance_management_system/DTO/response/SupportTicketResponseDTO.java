package tech.zeta.mavericks.digital_insurance_management_system.DTO.response;

import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.TicketStatus;

import java.sql.Date;
import java.util.List;

@Data
public class SupportTicketResponseDTO {
    private Long id;
    private Long userId;
    private Long policyId;
    private Long claimId;
    private String subject;
    private String description;
    private TicketStatus status;
    private Date createdAt;
    private Date resolvedAt;

    private List<MessageResponseDTO> messages;
}

