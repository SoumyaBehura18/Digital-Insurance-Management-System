package tech.zeta.mavericks.digital_insurance_management_system.dto.premium;

import lombok.Data;
import java.util.Set;

/**
 * Data Transfer Object for submitting health insurance premium details.
 *
 * This DTO is used when creating or updating health policy premiums,
 * including condition-based premiums and standard/renewal rates.
 */
@Data
public class HealthPremiumRequest {

    /** Set of condition-specific premiums for preexisting health conditions */
    private Set<ConditionPremium> conditionPremiums;

    /** Base premium rate for the health policy */
    private Double premiumRate;

    /** Renewal premium rate for the health policy */
    private Double renewalRate;
}
