package tech.zeta.mavericks.digital_insurance_management_system.DTO.request;

import lombok.Data;

@Data
public class SupportTicketUpdateDTO {
    private String response;  // Admin's reply
    private String status;    // RESOLVED or CLOSED
}

