package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.TicketStatus;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a support ticket submitted by a user.
 *
 * Stores information about the ticket's subject, description, status,
 * related user, optional policy or claim, associated messages, and timestamps.
 */
@Data
@Entity
@Table(name = "support_ticket")
public class SupportTicket {

    /** Unique ID of the support ticket */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** User who created the ticket */
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Optional policy associated with the ticket */
    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = true)
    private Policy policy;

    /** Optional claim associated with the ticket */
    @ManyToOne
    @JoinColumn(name = "claim_id", nullable = true)
    private Claim claim;

    /** Subject of the support ticket */
    @Column(nullable = false)
    private String subject;

    /** Detailed description of the support ticket */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    /** Current status of the ticket (e.g., OPEN, IN_PROGRESS, RESOLVED, CLOSED) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketStatus status;

    /** List of messages/responses associated with the ticket */
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> responses = new ArrayList<>();

    /** Timestamp when the ticket was created */
    @Column(nullable = false)
    private Timestamp createdAt;

    /** Timestamp when the ticket was resolved */
    private Timestamp resolvedAt;
}
