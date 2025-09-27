package tech.zeta.mavericks.digital_insurance_management_system.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPolicyResponse {
    private Long id;
    private Long userId;
    private Long policyId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Double premiumPaid;
    private String policyName;
    private PolicyType policyType;
    private Boolean noClaimBonus;
    private Double coverageAmount;

}

