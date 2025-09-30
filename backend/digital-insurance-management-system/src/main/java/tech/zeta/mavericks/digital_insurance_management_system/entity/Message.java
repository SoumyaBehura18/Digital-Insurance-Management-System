package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.MessageStatus;

import java.sql.Timestamp;

/**
 * Entity representing a message within a support ticket.
 *
 * Stores the author of the message, associated ticket, content, status, and timestamp.
 */
@Entity
@Table(name = "ticket_message")
@Data
public class Message {

    /** Unique ID of the message */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Support ticket to which this message belongs */
    @ManyToOne(optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private SupportTicket ticket;

    /** User or admin who authored the message */
    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    /** Text content of the message */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /** Status of the message (e.g., SENT, READ) */
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    /** Timestamp when the message was created */
    private Timestamp timestamp;
}
