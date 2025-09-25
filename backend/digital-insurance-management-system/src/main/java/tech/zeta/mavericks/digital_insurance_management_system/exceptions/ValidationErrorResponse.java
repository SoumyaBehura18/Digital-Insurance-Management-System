package tech.zeta.mavericks.digital_insurance_management_system.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class ValidationErrorResponse {
    private String errorCode;
    private String message;
    private Map<String, String> fieldErrors;
    private LocalDateTime timestamp;
}
