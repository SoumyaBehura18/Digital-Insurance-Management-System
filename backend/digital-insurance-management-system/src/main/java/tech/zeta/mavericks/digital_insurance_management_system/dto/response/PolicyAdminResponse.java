package tech.zeta.mavericks.digital_insurance_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyType;

/**
 * Data Transfer Object for returning policy details in the admin context.
 *
 * This DTO is used to provide information about a policy, including its type,
 * coverage, duration, and description, for administrative views and operations.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyAdminResponse {

    /** Unique ID of the policy */
    private Long policyId;

    /** Name of the policy */
    private String policyName;

    /** Description of the policy */
    private String description;

    /** Type of the policy (e.g., HEALTH, LIFE, VEHICLE) */
    private PolicyType policyType;

    /** Duration of the policy in months */
    private Integer duration;

    /** Coverage amount provided by the policy */
    private Double coverage;
}
