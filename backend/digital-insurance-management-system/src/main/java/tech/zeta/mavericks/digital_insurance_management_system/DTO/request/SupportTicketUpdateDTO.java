package tech.zeta.mavericks.digital_insurance_management_system.DTO.request;

import lombok.Data;

@Data
public class SupportTicketUpdateDTO {
    private String status;   // e.g., OPEN, IN_PROGRESS, RESOLVED, CLOSED
}


