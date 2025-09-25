package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
@Data
@Entity
@Table(name = "health_policy_premium")
public class HealthPolicyPremium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    @ToString.Exclude
    private Policy policy;

    private Double premiumRate;

    private Double renewalRate;

    private Integer startAge;

    private Integer endAge;

    private Boolean smokingDrinking;
}
