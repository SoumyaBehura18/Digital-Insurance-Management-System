package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyStatus;

import java.time.LocalDate;

/**
 * Entity representing a policy purchased by a user.
 *
 * Stores the associated user, policy, start and end dates, status,
 * premium paid, and whether the user has a no-claim bonus.
 */
@Data
@Entity
@Table(name = "user_policy")
public class UserPolicy {

    /** Unique ID of the user policy record */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** User who owns this policy */
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Policy associated with this user policy */
    @ManyToOne(optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    /** Start date of the policy coverage */
    private LocalDate startDate;

    /** End date of the policy coverage */
    private LocalDate endDate;

    /** Current status of the policy (e.g., ACTIVE, EXPIRED, CANCELLED) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PolicyStatus status;

    /** Premium amount paid by the user */
    private Double premiumPaid;

    /** Indicates whether the user has a no-claim bonus */
    private Boolean noClaimBonus;
}
