package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyType;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing an insurance policy.
 *
 * Stores the policy's name, description, coverage amount, duration, type,
 * and associated preexisting health conditions for health policies.
 */
@Data
@Entity
@Table(name = "policy")
public class Policy {

    /** Unique ID of the policy */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name of the policy */
    private String name;

    /** Description of the policy */
    @Column(columnDefinition = "TEXT")
    private String description;

    /** Coverage amount provided by the policy */
    private Double coverageAmt;

    /** Duration of the policy in months */
    private Integer durationMonths;

    /** Type of the policy (e.g., HEALTH, LIFE, VEHICLE) */
    @Enumerated(EnumType.STRING)
    private PolicyType type;

    /** List of preexisting conditions associated with this policy (for health policies) */
    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude  // Prevent recursion in toString
    private List<HealthPreexistingCondition> preexistingConditions = new ArrayList<>();
}
