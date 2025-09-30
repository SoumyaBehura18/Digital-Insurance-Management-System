package tech.zeta.mavericks.digital_insurance_management_system.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.zeta.mavericks.digital_insurance_management_system.controller.SupportTicketController;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.MessageRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.SupportTicketRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.SupportTicketUpdate;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.MessageResponse;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.SupportTicketResponse;
import tech.zeta.mavericks.digital_insurance_management_system.enums.TicketStatus;
import tech.zeta.mavericks.digital_insurance_management_system.service.SupportTicketService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SupportTicketTest {

    private MockMvc mockMvc;

    @Mock
    private SupportTicketService ticketService;

    private ObjectMapper objectMapper;

    private SupportTicketRequest supportTicketRequest;
    private SupportTicketResponse supportTicketResponse;
    private SupportTicketUpdate supportTicketUpdate;
    private MessageRequest messageRequest;
    private MessageResponse messageResponse;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new SupportTicketController(ticketService))
                .setControllerAdvice(new TestExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        initData();
    }

    private void initData() {
        supportTicketRequest = new SupportTicketRequest();
        supportTicketRequest.setUserId(1L);
        supportTicketRequest.setSubject("Login issue");
        supportTicketRequest.setDescription("Unable to login with valid credentials");

        messageResponse = new MessageResponse(
                200L,
                1L,
                "Please help me",
                Timestamp.from(Instant.now())
        );

        supportTicketResponse = new SupportTicketResponse();
        supportTicketResponse.setId(100L);
        supportTicketResponse.setUserId(1L);
        supportTicketResponse.setSubject("Login issue");
        supportTicketResponse.setDescription("Unable to login with valid credentials");
        supportTicketResponse.setStatus(TicketStatus.OPEN);
        supportTicketResponse.setCreatedAt(Timestamp.from(Instant.now()));
        supportTicketResponse.setMessages(List.of(messageResponse));

        supportTicketUpdate = new SupportTicketUpdate();
        supportTicketUpdate.setSubject("Updated subject");
        supportTicketUpdate.setDescription("Updated description");
        supportTicketUpdate.setStatus("RESOLVED");

        messageRequest = new MessageRequest();
        messageRequest.setAuthorId(1L);
        messageRequest.setContent("Please help me");
    }

    @Test
    void createTicket_ShouldReturnOk() throws Exception {
        when(ticketService.createSupportTicket(any(SupportTicketRequest.class)))
                .thenReturn(supportTicketResponse);

        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supportTicketRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.subject").value("Login issue"));
    }

    @Test
    void getAllTickets_ShouldReturnList() throws Exception {
        when(ticketService.getAllTickets()).thenReturn(Collections.singletonList(supportTicketResponse));

        mockMvc.perform(get("/tickets/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100L))
                .andExpect(jsonPath("$[0].status").value("OPEN"));
    }

    @Test
    void getAllTickets_ShouldReturnEmpty() throws Exception {
        when(ticketService.getAllTickets()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tickets/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getTicketById_ShouldReturnTicket() throws Exception {
        when(ticketService.getTicketByTicketId(100L)).thenReturn(supportTicketResponse);

        mockMvc.perform(get("/tickets/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.subject").value("Login issue"));
    }

    @Test
    void getTicketsByUserId_ShouldReturnList() throws Exception {
        when(ticketService.getTicketsByUserId(1L)).thenReturn(Collections.singletonList(supportTicketResponse));

        mockMvc.perform(get("/tickets/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].status").value("OPEN"));
    }

    @Test
    void updateTicket_ShouldReturnUpdated() throws Exception {
        when(ticketService.updateSupportTicket(eq(100L), any(SupportTicketUpdate.class)))
                .thenReturn(supportTicketResponse);

        mockMvc.perform(patch("/tickets/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supportTicketUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.status").value("OPEN")); // comes from mock
    }

    @Test
    void addMessage_ShouldReturnMessageResponse() throws Exception {
        when(ticketService.addMessage(eq(100L), any(MessageRequest.class)))
                .thenReturn(messageResponse);

        mockMvc.perform(post("/tickets/100/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(200L))
                .andExpect(jsonPath("$.content").value("Please help me"))
                .andExpect(jsonPath("$.authorId").value(1L));
    }

    @Test
    void updateTicket_ShouldReturnError_WhenServiceThrows() throws Exception {
        doThrow(new RuntimeException("ticket not found"))
                .when(ticketService)
                .updateSupportTicket(eq(999L), any(SupportTicketUpdate.class));

        mockMvc.perform(patch("/tickets/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supportTicketUpdate)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("ticket not found"));
    }

    @RestControllerAdvice
    static class TestExceptionHandler {
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
