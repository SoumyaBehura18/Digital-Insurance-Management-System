package tech.zeta.mavericks.digital_insurance_management_system.DTO.premium;

import lombok.Data;

@Data
public class VehiclePremiumRequestDTO {
    private int vehicleAge;
    private Double premiumRate;
    private Double renewalRate;
}