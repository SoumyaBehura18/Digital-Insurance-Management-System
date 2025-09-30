package tech.zeta.mavericks.digital_insurance_management_system.dto.request;

import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.ClaimStatus;

@Data
public class ClaimReview {
    private String reviewComments;
    private ClaimStatus status;
}
