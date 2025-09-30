package tech.zeta.mavericks.digital_insurance_management_system.dto.request;

import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.ClaimStatus;

/**
 * Data Transfer Object for reviewing an insurance claim.
 *
 * This DTO is used to capture review comments and status updates
 * for a claim before processing in the service layer.
 */
@Data
public class ClaimReview {

    /** Comments provided by the reviewer regarding the claim */
    private String reviewComments;

    /** Updated status of the claim */
    private ClaimStatus status;
}
