package tech.zeta.mavericks.digital_insurance_management_system.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data @AllArgsConstructor @NoArgsConstructor
public class MessageResponseDTO {
    private Long id;
    private Long authorId;
    private String content;
    private Date timestamp;
}

