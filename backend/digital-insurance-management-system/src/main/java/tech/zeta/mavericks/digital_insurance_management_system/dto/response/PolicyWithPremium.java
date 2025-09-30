package tech.zeta.mavericks.digital_insurance_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyType;

/**
 * Data Transfer Object for returning policy details along with premium information.
 *
 * This DTO is used to provide comprehensive policy information for users,
 * including base and renewal premiums, coverage, and duration.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyWithPremium {

    /** Unique ID of the policy */
    private Long policyId;

    /** Name of the policy */
    private String policyName;

    /** Type of the policy (e.g., HEALTH, LIFE, VEHICLE) */
    private PolicyType policyType;

    /** Base premium rate for the policy */
    private Double premiumRate;

    /** Renewal premium rate for the policy */
    private Double renewalRate;

    /** Duration of the policy in months */
    private Integer duration;

    /** Coverage amount provided by the policy */
    private Double coverage;
}
