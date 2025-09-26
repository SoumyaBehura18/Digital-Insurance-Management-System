package tech.zeta.mavericks.digital_insurance_management_system.DTO.request;

import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyStatus;

public class PolicyStatusRequest {
    private PolicyStatus policyStatus;
    public PolicyStatus getPolicyStatus() { return policyStatus; }
    public void setPolicyStatus(PolicyStatus policyStatus) { this.policyStatus = policyStatus; }
}

