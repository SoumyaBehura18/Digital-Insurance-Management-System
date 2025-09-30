package tech.zeta.mavericks.digital_insurance_management_system.dto.premium;

import lombok.Data;

/**
 * Data Transfer Object for submitting life insurance premium details.
 *
 * This DTO is used when creating or updating life policy premiums,
 * including base and renewal rates.
 */
@Data
public class LifePremiumRequest {

    /** Base premium rate for the life insurance policy */
    private Double premiumRate;

    /** Renewal premium rate for the life insurance policy */
    private Double renewalRate;
}
