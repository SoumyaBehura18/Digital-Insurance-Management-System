package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entity representing the premium details of a life insurance policy.
 *
 * Stores base and renewal premium rates, and whether the premium is affected
 * by the user's smoking or drinking habits. Linked to a specific Policy entity.
 */
@Data
@Entity
@Table(name = "life_policy_premium")
public class LifePolicyPremium {

    /** Unique ID of the life policy premium record */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Associated policy for which this premium applies */
    @ManyToOne(optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    /** Base premium rate for the life policy */
    private Double premiumRate;

    /** Renewal premium rate for the life policy */
    private Double renewalRate;

    /** Indicates whether smoking or drinking affects the premium */
    private Boolean smokingDrinking;
}
