package tech.zeta.mavericks.digital_insurance_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyType;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyAdminResponse {
    private Long policyId;
    private String policyName;
    private String description;
    private PolicyType policyType;
    private Integer duration;
    private Double coverage;
}
