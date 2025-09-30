package tech.zeta.mavericks.digital_insurance_management_system.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.MessageRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.SupportTicketRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.SupportTicketUpdate;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.MessageResponse;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.SupportTicketResponse;
import tech.zeta.mavericks.digital_insurance_management_system.entity.*;
import tech.zeta.mavericks.digital_insurance_management_system.enums.TicketStatus;
import tech.zeta.mavericks.digital_insurance_management_system.exception.*;
import tech.zeta.mavericks.digital_insurance_management_system.repository.*;
import tech.zeta.mavericks.digital_insurance_management_system.service.SupportTicketService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupportTicketServiceTest {

    @InjectMocks
    private SupportTicketService supportTicketService;

    @Mock
    private SupportTicketRepository supportTicketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private ClaimRepository claimRepository;

    private User user;
    private Policy policy;
    private Claim claim;
    private SupportTicket ticket;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);

        policy = new Policy();
        policy.setId(1L);

        claim = new Claim();
        claim.setId(1L);

        ticket = new SupportTicket();
        ticket.setId(1L);
        ticket.setUser(user);
        ticket.setSubject("Test Subject");
        ticket.setDescription("Test Description");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        ticket.setPolicy(policy);
        ticket.setClaim(claim);
        ticket.setResponses(new ArrayList<>());
    }

    @Test
    void createSupportTicket_ShouldReturnResponse() {
        SupportTicketRequest request = new SupportTicketRequest();
        request.setUserId(1L);
        request.setPolicyId(1L);
        request.setClaimId(1L);
        request.setSubject("Test Subject");
        request.setDescription("Test Description");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));
        when(supportTicketRepository.save(any(SupportTicket.class))).thenReturn(ticket);

        SupportTicketResponse response = supportTicketService.createSupportTicket(request);

        assertNotNull(response);
        assertEquals(ticket.getId(), response.getId());
        assertEquals("Test Subject", response.getSubject());
        assertEquals(TicketStatus.OPEN, response.getStatus());
    }

    @Test
    void createSupportTicket_ShouldThrowUserNotFound() {
        SupportTicketRequest request = new SupportTicketRequest();
        request.setUserId(99L);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> supportTicketService.createSupportTicket(request));
    }

    @Test
    void getAllTickets_ShouldReturnList() {
        when(supportTicketRepository.findAll()).thenReturn(Collections.singletonList(ticket));

        List<SupportTicketResponse> tickets = supportTicketService.getAllTickets();

        assertEquals(1, tickets.size());
        assertEquals(ticket.getId(), tickets.get(0).getId());
    }

    @Test
    void getTicketsByUserId_ShouldFilterByUserId() {
        SupportTicket otherTicket = new SupportTicket();
        User otherUser = new User();
        otherUser.setId(2L);
        otherTicket.setUser(otherUser);

        when(supportTicketRepository.findAll()).thenReturn(Arrays.asList(ticket, otherTicket));

        List<SupportTicketResponse> tickets = supportTicketService.getTicketsByUserId(1L);

        assertEquals(1, tickets.size());
        assertEquals(1L, tickets.get(0).getUserId());
    }

    @Test
    void getTicketByTicketId_ShouldReturnTicket() {
        when(supportTicketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        SupportTicketResponse response = supportTicketService.getTicketByTicketId(1L);

        assertEquals(ticket.getId(), response.getId());
    }

    @Test
    void getTicketByTicketId_ShouldThrowTicketNotFound() {
        when(supportTicketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class,
                () -> supportTicketService.getTicketByTicketId(99L));
    }

    @Test
    void updateSupportTicket_ShouldUpdateStatusAndDescription() {
        SupportTicketUpdate update = new SupportTicketUpdate();
        update.setStatus("RESOLVED");
        update.setDescription("Updated Desc");

        when(supportTicketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(supportTicketRepository.save(ticket)).thenReturn(ticket);

        SupportTicketResponse response = supportTicketService.updateSupportTicket(1L, update);

        assertEquals(TicketStatus.RESOLVED, response.getStatus());
        assertEquals("Updated Desc", response.getDescription());
        assertNotNull(response.getResolvedAt());
    }

    @Test
    void addMessage_ShouldReturnMessageResponse() {
        MessageRequest request = new MessageRequest();
        request.setAuthorId(1L);
        request.setContent("Hello");

        when(supportTicketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(supportTicketRepository.save(ticket)).thenReturn(ticket);

        MessageResponse response = supportTicketService.addMessage(1L, request);

        assertEquals(1L, response.getAuthorId());
        assertEquals("Hello", response.getContent());
    }

    @Test
    void addMessage_ShouldThrowTicketNotFound() {
        MessageRequest request = new MessageRequest();
        when(supportTicketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class,
                () -> supportTicketService.addMessage(99L, request));
    }

    @Test
    void addMessage_ShouldThrowUserNotFound() {
        MessageRequest request = new MessageRequest();
        request.setAuthorId(99L);

        when(supportTicketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> supportTicketService.addMessage(1L, request));
    }
}
