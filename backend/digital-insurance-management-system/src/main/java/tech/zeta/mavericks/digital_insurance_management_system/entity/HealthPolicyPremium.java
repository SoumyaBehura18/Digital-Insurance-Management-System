package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;

import java.util.Set;

@Data
@Entity
@Table(name = "health_policy_premium")
public class HealthPolicyPremium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    private Double premiumRate;

    private Double renewalRate;

    private Integer startAge;

    private Integer endAge;

    private Boolean smokingDrinking;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "health_preexisting_conditions",
            joinColumns = @JoinColumn(name = "health_premium_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "condition")
    private Set<HealthCondition> preexistingDiseases;

}
