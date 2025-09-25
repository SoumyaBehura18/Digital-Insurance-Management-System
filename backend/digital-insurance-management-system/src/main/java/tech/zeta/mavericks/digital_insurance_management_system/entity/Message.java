package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.MessageStatus;

import java.sql.Date;

@Entity
@Table(name = "ticket_messages")
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to ticket
    @ManyToOne(optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private SupportTicket ticket;

    // Who wrote the message (User or Admin)
    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    private Date timestamp;
}
