package tech.zeta.mavericks.digital_insurance_management_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyStatus;

import java.time.LocalDate;

/**
 * Data Transfer Object for updating the status and premium of a user policy.
 *
 * This DTO is used when modifying a policy's status (e.g., ACTIVE, LAPSED)
 * and updating its premium rate in the system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyStatusRequest {

    /** Updated premium rate for the policy */
    private Double premiumRate;

    /** New status of the policy */
    private PolicyStatus policyStatus;

    /** New startDate of the policy */
    private LocalDate startDate;

    /** New endDate of the policy */
    private LocalDate endDate;
}
