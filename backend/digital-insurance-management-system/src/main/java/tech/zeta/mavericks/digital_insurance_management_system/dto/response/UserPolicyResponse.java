package tech.zeta.mavericks.digital_insurance_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyType;

import java.time.LocalDate;

/**
 * Data Transfer Object for returning details of a user's purchased policy.
 *
 * This DTO is used to provide comprehensive information about a user policy,
 * including policy details, coverage, premium paid, status, and no-claim bonus.
 */
@Data
public class UserPolicyResponse {

    /** Unique ID of the user policy */
    private Long id;

    /** ID of the user associated with the policy */
    private Long userId;

    /** ID of the policy */
    private Long policyId;

    /** Start date of the policy coverage */
    private LocalDate startDate;

    /** End date of the policy coverage */
    private LocalDate endDate;

    /** Current status of the policy (e.g., ACTIVE, LAPSED) */
    private String status;

    /** Premium amount paid for the policy */
    private Double premiumPaid;

    /** Name of the policy */
    private String policyName;

    /** Type of the policy (e.g., HEALTH, LIFE, VEHICLE) */
    private PolicyType policyType;

    /** Indicates whether the policy has a no-claim bonus */
    private Boolean noClaimBonus;

    /** Total coverage amount provided by the policy */
    private Double coverageAmount;
}
