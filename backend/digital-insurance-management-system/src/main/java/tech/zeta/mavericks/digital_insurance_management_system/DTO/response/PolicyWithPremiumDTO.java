package tech.zeta.mavericks.digital_insurance_management_system.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyWithPremiumDTO {
    private Long policyId;
    private String policyName;
    private PolicyType policyType;
    private Double premiumRate;
    private Double renewalRate;
    private Integer duration;

}
