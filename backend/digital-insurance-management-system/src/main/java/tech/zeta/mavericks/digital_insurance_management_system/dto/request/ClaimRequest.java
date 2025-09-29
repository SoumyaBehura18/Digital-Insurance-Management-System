package tech.zeta.mavericks.digital_insurance_management_system.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ClaimRequest {

    @NotNull(message = "User policy ID is required")
    private Long userPolicyId;

    @NotNull(message = "Claim date is required")
    private LocalDate claimDate;

    @NotNull(message = "Claim amount is required")
    @DecimalMin(value = "0.01", message = "Claim amount must be greater than 0")
    private BigDecimal claimAmount;

    @NotBlank(message = "Reason is required")
    @Size(min = 10, max = 500, message = "Reason must be between 10 and 500 characters")
    private String reason;
}
