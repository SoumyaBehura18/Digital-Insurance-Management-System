package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;

/**
 * Entity representing the premium details of a health insurance policy.
 *
 * Stores base and renewal premium rates, and whether the premium is affected
 * by the user's smoking or drinking habits. Linked to a specific Policy entity.
 */
@Data
@Entity
@Table(name = "health_policy_premium")
public class HealthPolicyPremium {

    /** Unique ID of the health policy premium record */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Associated policy for which this premium applies */
    @ManyToOne(optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    @ToString.Exclude
    private Policy policy;

    /** Base premium rate for the health policy */
    private Double premiumRate;

    /** Renewal premium rate for the health policy */
    private Double renewalRate;

    /** Indicates whether smoking or drinking affects the premium */
    private Boolean smokingDrinking;
}
