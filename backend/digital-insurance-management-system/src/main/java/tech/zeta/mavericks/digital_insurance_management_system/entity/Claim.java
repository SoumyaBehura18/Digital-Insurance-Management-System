package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.ClaimStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity representing an insurance claim in the system.
 * Maps to the 'claim' table in the database.
 *
 * This entity tracks insurance claims submitted by users for their policies,
 * including claim details, status, and supporting documentation.
 *
 * @author Team Mavericks
 */
@Data
@Entity
@Table(name = "claim")
public class Claim {
    /** Primary key - Auto-generated unique identifier for the claim */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Many-to-one relationship with UserPolicy - The policy being claimed against */
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_policy_id", nullable = false)
    private UserPolicy userPolicy;

    /** Date when the claim incident occurred */
    @Column(nullable = false)
    private LocalDate claimDate;

    /** Amount being claimed by the user (precision 12, scale 2 for currency) */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal claimAmount;

    /** Detailed reason/description for the claim */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    /** Current status of the claim (PENDING, APPROVED, REJECTED) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClaimStatus status;

    /** Comments from the claim reviewer/administrator */
    @Column(columnDefinition = "TEXT")
    private String reviewerComment;

    /** Date when the claim was resolved (approved/rejected) */
    private LocalDate resolvedDate;

    /** URL link to the supporting document stored in Supabase */
    @Column(columnDefinition = "TEXT")
    private String documentLink;

}