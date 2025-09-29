package tech.zeta.mavericks.digital_insurance_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Timestamp;

@Data @AllArgsConstructor @NoArgsConstructor
public class MessageResponse {
    private Long id;
    private Long authorId;
    private String content;
    private Timestamp timestamp;
}

