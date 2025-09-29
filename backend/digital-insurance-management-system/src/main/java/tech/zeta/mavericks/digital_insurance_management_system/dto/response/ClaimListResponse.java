package tech.zeta.mavericks.digital_insurance_management_system.dto.response;

import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.ClaimStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ClaimListResponse {
    private Long id;
    private Long userPolicyId;
    private Long userId;
    private String userName;
    private String userEmail;
    private String policyName;
    private LocalDate claimDate;
    private BigDecimal claimAmount;
    private String reason;
    private ClaimStatus status;
    private String reviewerComment;
    private LocalDate resolvedDate;

    @Data
    public static class ClaimResponseDto {
        private Long id;
        private Long userPolicyId;
        private LocalDate claimDate;
        private BigDecimal claimAmount;
        private String reason;
        private ClaimStatus status;
        private String reviewerComment;
        private LocalDate resolvedDate;
    }
}
