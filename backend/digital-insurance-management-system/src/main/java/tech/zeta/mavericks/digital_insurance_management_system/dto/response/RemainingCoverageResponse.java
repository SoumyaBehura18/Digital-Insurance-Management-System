package tech.zeta.mavericks.digital_insurance_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemainingCoverageResponse {
    private Long policyId;
    private Double remainingClaimAmount;
}

