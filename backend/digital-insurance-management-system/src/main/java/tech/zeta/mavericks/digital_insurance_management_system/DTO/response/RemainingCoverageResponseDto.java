package tech.zeta.mavericks.digital_insurance_management_system.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemainingCoverageResponseDto {
    private Long policyId;
    private Double remainingClaimAmount;
}

