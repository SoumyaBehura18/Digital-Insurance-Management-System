package tech.zeta.mavericks.digital_insurance_management_system.DTO.request;

import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Claim;
import tech.zeta.mavericks.digital_insurance_management_system.enums.ClaimStatus;

@Data
public class ClaimReviewDTO {
    private String reviewComments;
    private ClaimStatus status;
}
