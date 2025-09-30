package tech.zeta.mavericks.digital_insurance_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for returning the remaining claimable amount for a policy.
 *
 * This DTO is used to provide information about how much coverage is still available
 * for a specific user policy.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemainingCoverageResponse {

    /** Unique ID of the policy */
    private Long policyId;

    /** Remaining claimable amount for the policy */
    private Double remainingClaimAmount;
}
