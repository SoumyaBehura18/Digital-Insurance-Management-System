package tech.zeta.mavericks.digital_insurance_management_system.dto.request;

import lombok.Data;
import java.time.LocalDate;

/**
 * Data Transfer Object for submitting or updating a user's policy details.
 *
 * This DTO is used to capture all necessary information when a user purchases
 * a policy or when updating user policy details such as status, dates, and premiums.
 */
@Data
public class UserPolicyRequest {

    /** ID of the user associated with the policy */
    private Long userId;

    /** ID of the policy being purchased or updated */
    private Long policyId;

    /** Start date of the policy coverage */
    private LocalDate startDate;

    /** End date of the policy coverage */
    private LocalDate endDate;

    /** Current status of the policy (e.g., ACTIVE, LAPSED) */
    private String status;

    /** Premium amount paid for the policy */
    private Double premiumPaid;
}
