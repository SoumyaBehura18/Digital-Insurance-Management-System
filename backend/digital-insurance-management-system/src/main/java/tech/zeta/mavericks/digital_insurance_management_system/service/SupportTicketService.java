package tech.zeta.mavericks.digital_insurance_management_system.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.MessageRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.SupportTicketRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.SupportTicketUpdate;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.MessageResponse;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.SupportTicketResponse;
import tech.zeta.mavericks.digital_insurance_management_system.exception.ClaimNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.exception.PolicyNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.exception.TicketNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.exception.UserNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.entity.*;
import tech.zeta.mavericks.digital_insurance_management_system.enums.TicketStatus;
import tech.zeta.mavericks.digital_insurance_management_system.repository.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing support tickets.
 * Handles ticket creation, updates, messaging, and retrieval.
 */
@Service
public class SupportTicketService {

    private static final Logger logger = LoggerFactory.getLogger(SupportTicketService.class);

    @Autowired
    private SupportTicketRepository supportTicketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private ClaimRepository claimRepository;

    /**
     * Creates a new support ticket.
     *
     * @param requestDTO the support ticket request data
     * @return the saved support ticket as a DTO
     */
    public SupportTicketResponse createSupportTicket(SupportTicketRequest requestDTO) {
        logger.info("Creating support ticket for user ID: {}", requestDTO.getUserId());

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> {
                    logger.warn("User not found with ID: {}", requestDTO.getUserId());
                    return new UserNotFoundException("User not found with id: " + requestDTO.getUserId());
                });

        Policy policy = null;
        if (requestDTO.getPolicyId() != null) {
            policy = policyRepository.findById(requestDTO.getPolicyId())
                    .orElseThrow(() -> {
                        logger.warn("Policy not found with ID: {}", requestDTO.getPolicyId());
                        return new PolicyNotFoundException("Policy not found with id: " + requestDTO.getPolicyId());
                    });
        }

        Claim claim = null;
        if (requestDTO.getClaimId() != null) {
            claim = claimRepository.findById(requestDTO.getClaimId())
                    .orElseThrow(() -> {
                        logger.warn("Claim not found with ID: {}", requestDTO.getClaimId());
                        return new ClaimNotFoundException("Claim not found with id: " + requestDTO.getClaimId());
                    });
        }

        SupportTicket ticket = new SupportTicket();
        ticket.setUser(user);
        ticket.setPolicy(policy);
        ticket.setClaim(claim);
        ticket.setSubject(requestDTO.getSubject());
        ticket.setDescription(requestDTO.getDescription());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        SupportTicket saved = supportTicketRepository.save(ticket);
        logger.info("Support ticket created with ID: {}", saved.getId());

        return mapToResponseDTO(saved);
    }

    /**
     * Retrieves all support tickets.
     *
     * @return list of all support tickets as DTOs
     */
    public List<SupportTicketResponse> getAllTickets() {
        logger.info("Fetching all support tickets");
        return supportTicketRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all tickets submitted by a specific user.
     *
     * @param userId the user's ID
     * @return list of user's tickets as DTOs
     */
    public List<SupportTicketResponse> getTicketsByUserId(Long userId) {
        logger.info("Fetching tickets for user ID: {}", userId);
        return supportTicketRepository.findAll().stream()
                .filter(ticket -> ticket.getUser().getId().equals(userId))
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single ticket by its ID.
     *
     * @param ticketId the ticket's ID
     * @return the ticket as a DTO
     */
    public SupportTicketResponse getTicketByTicketId(Long ticketId) {
        logger.info("Fetching ticket with ID: {}", ticketId);
        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> {
                    logger.warn("Ticket not found with ID: {}", ticketId);
                    return new TicketNotFoundException("Ticket not found with id: " + ticketId);
                });
        return mapToResponseDTO(ticket);
    }

    /**
     * Updates a support ticket with new data.
     *
     * @param ticketId  the ticket ID
     * @param updateDTO the update data
     * @return updated ticket as a DTO
     */
    @Transactional
    public SupportTicketResponse updateSupportTicket(Long ticketId, SupportTicketUpdate updateDTO) {
        logger.info("Updating ticket with ID: {}", ticketId);
        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> {
                    logger.warn("Ticket not found with ID: {}", ticketId);
                    return new TicketNotFoundException("Ticket not found with id: " + ticketId);
                });

        if (updateDTO.getSubject() != null) ticket.setSubject(updateDTO.getSubject());
        if (updateDTO.getDescription() != null) ticket.setDescription(updateDTO.getDescription());

        if (updateDTO.getPolicyId() != null) {
            Policy policy = policyRepository.findById(updateDTO.getPolicyId())
                    .orElseThrow(() -> new PolicyNotFoundException("Policy not found"));
            ticket.setPolicy(policy);
        }

        if (updateDTO.getClaimId() != null) {
            Claim claim = claimRepository.findById(updateDTO.getClaimId())
                    .orElseThrow(() -> new ClaimNotFoundException("Claim not found"));
            ticket.setClaim(claim);
        }

        if (updateDTO.getStatus() != null) {
            TicketStatus newStatus = TicketStatus.valueOf(updateDTO.getStatus().toUpperCase());
            ticket.setStatus(newStatus);
            if (newStatus == TicketStatus.RESOLVED || newStatus == TicketStatus.CLOSED) {
                ticket.setResolvedAt(Timestamp.valueOf(LocalDateTime.now()));
            }
        }

        SupportTicket updated = supportTicketRepository.save(ticket);
        logger.info("Ticket updated with ID: {}", ticketId);

        return mapToResponseDTO(updated);
    }

    /**
     * Adds a message to a ticket.
     *
     * @param ticketId the ticket ID
     * @param request  the message request
     * @return the saved message as a DTO
     */
    public MessageResponse addMessage(Long ticketId, MessageRequest request) {
        logger.info("Adding message to ticket ID: {}", ticketId);

        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with id: " + ticketId));

        User author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getAuthorId()));

        Message message = new Message();
        message.setTicket(ticket);
        message.setAuthor(author);
        message.setContent(request.getContent());
        message.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));

        ticket.getResponses().add(message);
        supportTicketRepository.save(ticket);

        logger.info("Message added to ticket ID: {}", ticketId);

        return new MessageResponse(
                message.getId(),
                author.getId(),
                message.getContent(),
                message.getTimestamp()
        );
    }

    /**
     * Maps a SupportTicket entity to a SupportTicketResponse DTO.
     *
     * @param ticket the SupportTicket entity
     * @return the SupportTicketResponse DTO
     */
    private SupportTicketResponse mapToResponseDTO(SupportTicket ticket) {
        SupportTicketResponse dto = new SupportTicketResponse();
        dto.setId(ticket.getId());
        dto.setUserId(ticket.getUser().getId());
        dto.setPolicyId(ticket.getPolicy() != null ? ticket.getPolicy().getId() : null);
        dto.setClaimId(ticket.getClaim() != null ? ticket.getClaim().getId() : null);
        dto.setSubject(ticket.getSubject());
        dto.setDescription(ticket.getDescription());
        dto.setStatus(ticket.getStatus());
        dto.setCreatedAt(ticket.getCreatedAt());
        dto.setResolvedAt(ticket.getResolvedAt());

        List<MessageResponse> messages = ticket.getResponses().stream()
                .map(m -> new MessageResponse(
                        m.getId(),
                        m.getAuthor().getId(),
                        m.getContent(),
                        m.getTimestamp()
                ))
                .collect(Collectors.toList());

        dto.setMessages(messages);
        return dto;
    }
}
