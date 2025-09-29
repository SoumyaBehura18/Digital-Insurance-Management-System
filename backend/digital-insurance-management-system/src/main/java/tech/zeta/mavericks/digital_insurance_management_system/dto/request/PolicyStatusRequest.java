package tech.zeta.mavericks.digital_insurance_management_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyStatus;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyStatusRequest {
    private Double premiumRate;
    private PolicyStatus policyStatus;
}

