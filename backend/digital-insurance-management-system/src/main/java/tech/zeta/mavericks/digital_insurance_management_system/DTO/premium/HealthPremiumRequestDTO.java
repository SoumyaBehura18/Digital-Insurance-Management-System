package tech.zeta.mavericks.digital_insurance_management_system.dto.premium;
import lombok.Data;
import java.util.Set;

@Data
public class HealthPremiumRequestDTO {
    private Set<ConditionPremiumDTO> conditionPremiums;
    private Double premiumRate;
    private Double renewalRate;
}

