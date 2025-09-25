package tech.zeta.mavericks.digital_insurance_management_system.dto;

import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.ClaimStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ClaimResponseDto {
    private Long id;
    private Long userPolicyId;
    private LocalDate claimDate;
    private BigDecimal claimAmount;
    private String reason;
    private ClaimStatus status;
    private String reviewerComment;
    private LocalDate resolvedDate;
}
