package tech.zeta.mavericks.digital_insurance_management_system.DTO.request;

import lombok.Data;

@Data
public class MessageRequestDTO {
    private Long authorId;   // ID of the user or admin sending the message
    private String content;  // Message text
}
