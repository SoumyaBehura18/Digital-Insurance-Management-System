package tech.zeta.mavericks.digital_insurance_management_system.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.MessageRequestDTO;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.SupportTicketRequestDTO;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.SupportTicketUpdateDTO;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.MessageResponseDTO;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.SupportTicketResponseDTO;
import tech.zeta.mavericks.digital_insurance_management_system.exception.ClaimNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.exception.PolicyNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.exception.TicketNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.exception.UserNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.entity.*;
import tech.zeta.mavericks.digital_insurance_management_system.repository.ClaimRepository;
import tech.zeta.mavericks.digital_insurance_management_system.repository.PolicyRepository;
import tech.zeta.mavericks.digital_insurance_management_system.repository.SupportTicketRepository;
import tech.zeta.mavericks.digital_insurance_management_system.repository.UserRepository;
import tech.zeta.mavericks.digital_insurance_management_system.enums.TicketStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupportTicketService {

    @Autowired
    private SupportTicketRepository supportTicketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private ClaimRepository claimRepository;

    public SupportTicketResponseDTO createSupportTicket(SupportTicketRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + requestDTO.getUserId()));

        Policy policy = null;
        if (requestDTO.getPolicyId() != null) {
            policy = policyRepository.findById(requestDTO.getPolicyId())
                    .orElseThrow(() -> new PolicyNotFoundException("Policy not found with id: " + requestDTO.getPolicyId()));
        }

        Claim claim = null;
        if (requestDTO.getClaimId() != null) {
            claim = claimRepository.findById(requestDTO.getClaimId())
                    .orElseThrow(() -> new ClaimNotFoundException("Claim not found with id: " + requestDTO.getClaimId()));
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

        return mapToResponseDTO(saved);
    }

    /**
     * Get all tickets for the admin
     */
    public List<SupportTicketResponseDTO> getAllTickets() {
        List<SupportTicket> tickets = supportTicketRepository.findAll().stream()
                .collect(Collectors.toList());

        return tickets.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all tickets submitted by a user
     */
    public List<SupportTicketResponseDTO> getTicketsByUserId(Long userId) {
        List<SupportTicket> tickets = supportTicketRepository.findAll().stream()
                .filter(ticket -> ticket.getUser().getId().equals(userId))
                .collect(Collectors.toList());

        return tickets.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public SupportTicketResponseDTO getTicketByTicketId(Long ticketId) {
        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with id: " + ticketId));

        return mapToResponseDTO(ticket);
    }

    /**
     * Update ticket with admin response and status
     */
    @Transactional
    public SupportTicketResponseDTO updateSupportTicket(Long ticketId, SupportTicketUpdateDTO updateDTO) {
        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with id: " + ticketId));

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
        return mapToResponseDTO(updated);
    }


    public MessageResponseDTO addMessage(Long ticketId, MessageRequestDTO request) {
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

        return new MessageResponseDTO(
                message.getId(),
                author.getId(),
                message.getContent(),
                message.getTimestamp()
        );
    }

    /**
     * Mapper: Entity → DTO
     */
    private SupportTicketResponseDTO mapToResponseDTO(SupportTicket ticket) {
        SupportTicketResponseDTO dto = new SupportTicketResponseDTO();
        dto.setId(ticket.getId());
        dto.setUserId(ticket.getUser().getId());
        dto.setPolicyId(ticket.getPolicy() != null ? ticket.getPolicy().getId() : null);
        dto.setClaimId(ticket.getClaim() != null ? ticket.getClaim().getId() : null);
        dto.setSubject(ticket.getSubject());
        dto.setDescription(ticket.getDescription());
        dto.setStatus(ticket.getStatus());
        dto.setCreatedAt(ticket.getCreatedAt());
        dto.setResolvedAt(ticket.getResolvedAt());

        // map messages
        List<MessageResponseDTO> messages = ticket.getResponses().stream().map(m -> {
            MessageResponseDTO messageResponseDTO = new MessageResponseDTO();
            messageResponseDTO.setId(m.getId());
            messageResponseDTO.setAuthorId(m.getAuthor().getId());
            messageResponseDTO.setContent(m.getContent());
            messageResponseDTO.setTimestamp(m.getTimestamp());
            return messageResponseDTO;
        }).toList();

        dto.setMessages(messages);

        return dto;
    }

}

