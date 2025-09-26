package tech.zeta.mavericks.digital_insurance_management_system.dto.premium;

import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
@Data
public class ConditionPremiumDTO {
    private HealthCondition condition;
    private Double extraPremium;


}
