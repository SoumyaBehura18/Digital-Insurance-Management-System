<<<<<<< Updated upstream:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/entity/Policy.java
package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyType;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "policies")
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

=======
package tech.zeta.mavericks.digital_insurance_management_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "policy")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private Long id;

    @Column(nullable = false)
>>>>>>> Stashed changes:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/model/Policy.java
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

<<<<<<< Updated upstream:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/entity/Policy.java
    private Double coverageAmt;

    private Integer durationMonths;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private PolicyType type;

}
=======
    @Column(name = "premium_amount", nullable = false)
    private BigDecimal premiumAmount;

    @Column(name = "coverage_amount", nullable = false)
    private BigDecimal coverageAmount;

    @Column(name = "duration_months", nullable = false)
    private Integer durationMonths;

    @Column(name = "renewal_premium_rate", nullable = false)
    private BigDecimal renewalPremiumRate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}

>>>>>>> Stashed changes:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/model/Policy.java
