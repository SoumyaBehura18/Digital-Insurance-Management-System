package tech.zeta.mavericks.digital_insurance_management_system.dto.request;

import lombok.Data;

@Data
public class SupportTicketRequest {
    private Long userId;
    private Long policyId;  // Optional
    private Long claimId;   // Optional
    private String subject;
    private String description;
}

