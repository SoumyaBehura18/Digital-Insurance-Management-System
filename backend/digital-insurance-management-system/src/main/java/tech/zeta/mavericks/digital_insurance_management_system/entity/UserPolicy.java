<<<<<<< Updated upstream:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/entity/UserPolicy.java
package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyStatus;

import java.time.LocalDate;

@Entity
@Table(name = "user_policies")
=======
package tech.zeta.mavericks.digital_insurance_management_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "user_policy")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
>>>>>>> Stashed changes:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/model/UserPolicy.java
public class UserPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
<<<<<<< Updated upstream:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/entity/UserPolicy.java
    private Long id;

    // Relationship to User
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relationship to Policy
    @ManyToOne(optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PolicyStatus status;


}
=======
    @Column(name = "user_policy_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String status;

    @Column(name = "premium_paid", nullable = false)
    private BigDecimal premiumPaid;

}


>>>>>>> Stashed changes:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/model/UserPolicy.java
