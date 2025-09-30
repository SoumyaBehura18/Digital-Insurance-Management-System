package tech.zeta.mavericks.digital_insurance_management_system.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object for claim submission requests.
 * Contains all required information for creating a new insurance claim.
 *
 * This DTO is used to capture and validate claim data from API requests
 * before processing in the service layer.
 *
 * @author Team Mavericks
 */
@Data
public class ClaimRequest {

    /** ID of the user policy for which the claim is being submitted */
    @NotNull(message = "User policy ID is required")
    private Long userPolicyId;

    /** Date when the claim incident occurred */
    @NotNull(message = "Claim date is required")
    private LocalDate claimDate;

    /** Amount being claimed by the user (must be greater than 0) */
    @NotNull(message = "Claim amount is required")
    @DecimalMin(value = "0.01", message = "Claim amount must be greater than 0")
    private BigDecimal claimAmount;

    /** Detailed reason or description for the claim (10-500 characters) */
    @NotBlank(message = "Reason is required")
    @Size(min = 10, max = 500, message = "Reason must be between 10 and 500 characters")
    private String reason;
}
