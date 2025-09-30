package tech.zeta.mavericks.digital_insurance_management_system.dto.premium;

import lombok.Data;

/**
 * Data Transfer Object for submitting vehicle insurance premium details.
 *
 * This DTO is used when creating or updating vehicle policy premiums,
 * including vehicle age, base premium, and renewal rate.
 */
@Data
public class VehiclePremiumRequest {

    /** Age of the vehicle in years, used for premium calculation */
    private int vehicleAge;

    /** Base premium rate for the vehicle insurance policy */
    private Double premiumRate;

    /** Renewal premium rate for the vehicle insurance policy */
    private Double renewalRate;
}
