package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.TicketStatus;

import java.sql.Date;

@Data
@Entity
@Table(name = "support_tickets")
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @JoinColumn(name = "claim_id")
    private Claim claim;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketStatus status;

    @Column(columnDefinition = "TEXT")
    private String response;

    @Column(nullable = false)
    private Date createdAt;

    private Date resolvedAt;

}