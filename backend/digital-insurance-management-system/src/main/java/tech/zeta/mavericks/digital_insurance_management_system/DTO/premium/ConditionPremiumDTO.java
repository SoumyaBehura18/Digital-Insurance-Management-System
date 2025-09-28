package tech.zeta.mavericks.digital_insurance_management_system.dto.premium;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConditionPremiumDTO {
    private HealthCondition condition;
    private Double extraPremium;
}
