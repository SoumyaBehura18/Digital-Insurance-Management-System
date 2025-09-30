package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;

/**
 * Entity representing preexisting health conditions associated with a policy.
 *
 * Stores the condition, any additional premium charged due to the condition,
 * and links the condition to a specific Policy entity.
 */
@Data
@Entity
@Table(name = "health_preexisting_conditions")
public class HealthPreexistingCondition {

    /** Unique ID of the preexisting condition record */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Specific health condition (e.g., DIABETES, HYPERTENSION) */
    @Enumerated(EnumType.STRING)
    @Column(name = "condition", nullable = false)
    private HealthCondition condition;

    /** Additional premium associated with this condition */
    @Column(name = "additional_premium", nullable = false)
    private Double additionalPremium = 0.0;

    /** Policy to which this preexisting condition applies */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    @ToString.Exclude  // Prevent recursion in toString
    private Policy policy;
}
