<<<<<<< Updated upstream:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/entity/Claim.java
package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.ClaimStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@Entity
@Table(name = "claims")
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_policy_id", nullable = false)
    private UserPolicy userPolicy;

    @Column(nullable = false)
    private LocalDate claimDate;

    @Column(nullable = false, precision = 12, scale = 2)
=======
package tech.zeta.mavericks.digital_insurance_management_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "claim")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claim_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_policy_id", nullable = false)
    private UserPolicy userPolicy;

    @Column(name = "claim_date", nullable = false)
    private LocalDate claimDate;

    @Column(name = "claim_amount", nullable = false)
>>>>>>> Stashed changes:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/model/Claim.java
    private BigDecimal claimAmount;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

<<<<<<< Updated upstream:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/entity/Claim.java
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClaimStatus status;

    @Column(columnDefinition = "TEXT")
    private String reviewerComment;

    private LocalDate resolvedDate;


=======
    @Column(nullable = false)
    private String status;  // Enum suggestion: PENDING, APPROVED, REJECTED

    @Column(name = "reviewer_comment", columnDefinition = "TEXT")
    private String reviewerComment;

    @Column(name = "resolved_date")
    private LocalDate resolvedDate;

>>>>>>> Stashed changes:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/model/Claim.java
}

