<<<<<<< Updated upstream:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/entity/SupportTicket.java
package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.TicketStatus;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "support_tickets")
=======
package tech.zeta.mavericks.digital_insurance_management_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "support_ticket")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
>>>>>>> Stashed changes:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/model/SupportTicket.java
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
<<<<<<< Updated upstream:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/entity/SupportTicket.java
    private Long id;

    // User who created the ticket
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Optional policy related to the ticket
    @ManyToOne
    @JoinColumn(name = "policy_id")
    private Policy policy;

    // Optional claim related to the ticket
    @ManyToOne
=======
    @Column(name = "ticket_id")
    private Long id;

    // Many-to-One with User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    // Optional Many-to-One with Policy
    @ManyToOne(optional = true)
    @JoinColumn(name = "policy_id")
    private Policy policy;

    // Optional Many-to-One with Claim
    @ManyToOne(optional = true)
>>>>>>> Stashed changes:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/model/SupportTicket.java
    @JoinColumn(name = "claim_id")
    private Claim claim;

    @Column(nullable = false)
    private String subject;

<<<<<<< Updated upstream:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/entity/SupportTicket.java
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
=======
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
>>>>>>> Stashed changes:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/model/SupportTicket.java
    private TicketStatus status;

    @Column(columnDefinition = "TEXT")
    private String response;

<<<<<<< Updated upstream:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/entity/SupportTicket.java
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime resolvedAt;
=======
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "resolved_at")
    private Date resolvedAt;
>>>>>>> Stashed changes:backend/digital-insurance-management-system/src/main/java/tech/zeta/mavericks/digital_insurance_management_system/model/SupportTicket.java

}
