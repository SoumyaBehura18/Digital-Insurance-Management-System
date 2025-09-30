package tech.zeta.mavericks.digital_insurance_management_system.dto.premium;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;

/**
 * Data Transfer Object representing additional premium for a specific health condition.
 *
 * This DTO is used when calculating health insurance premiums based on preexisting conditions.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConditionPremium {

    /** Health condition associated with the extra premium */
    private HealthCondition condition;

    /** Additional premium amount for the specified health condition */
    private Double extraPremium;
}
