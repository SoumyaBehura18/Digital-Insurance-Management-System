package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;

@Data
@Entity
@Table(name = "health_preexisting_conditions")
public class HealthPreexistingCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition", nullable = false)
    private HealthCondition condition;

    @Column(name = "additional_premium", nullable = false)
    private Double additionalPremium = 0.0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    @ToString.Exclude  // Prevent recursion
    private Policy policy;
}

