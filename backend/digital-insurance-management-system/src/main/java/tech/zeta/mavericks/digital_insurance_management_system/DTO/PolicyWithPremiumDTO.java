package tech.zeta.mavericks.digital_insurance_management_system.dto;

import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyType;

public class PolicyWithPremiumDTO {
    private Long policyId;
    private String policyName;
    private PolicyType policyType;
    private Double premiumRate;
    private Double renewalRate;

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public PolicyType getPolicyType() {
        return policyType;
    }

    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
    }

    public Double getPremiumRate() {
        return premiumRate;
    }

    public void setPremiumRate(Double premiumRate) {
        this.premiumRate = premiumRate;
    }

    public Double getRenewalRate() {
        return renewalRate;
    }

    public void setRenewalRate(Double renewalRate) {
        this.renewalRate = renewalRate;
    }

    public PolicyWithPremiumDTO(Long policyId, String policyName, PolicyType policyType,
                                Double premiumRate, Double renewalRate) {
        this.policyId = policyId;
        this.policyName = policyName;
        this.policyType = policyType;
        this.premiumRate = premiumRate;
        this.renewalRate = renewalRate;
    }

}
