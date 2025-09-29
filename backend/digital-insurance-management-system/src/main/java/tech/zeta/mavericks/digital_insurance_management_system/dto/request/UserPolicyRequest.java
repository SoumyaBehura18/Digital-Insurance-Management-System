package tech.zeta.mavericks.digital_insurance_management_system.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserPolicyRequest {
    private Long userId;
    private Long policyId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Double premiumPaid;
}
