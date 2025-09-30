package tech.zeta.mavericks.digital_insurance_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.MessageRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.SupportTicketRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.SupportTicketUpdate;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.MessageResponse;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.SupportTicketResponse;
import tech.zeta.mavericks.digital_insurance_management_system.service.SupportTicketService;

import java.util.List;

/**
 * REST controller for managing support tickets.
 * Provides endpoints for creating, retrieving, updating, and messaging within support tickets.
 *
 * Endpoints:
 *  - POST /tickets: Create a new support ticket
 *  - GET /tickets/all: Retrieve all support tickets (admin)
 *  - GET /tickets/{id}: Get a specific support ticket by ID
 *  - GET /tickets/user/{id}: Get all support tickets for a specific user
 *  - PATCH /tickets/{id}: Update a support ticket's status or details
 *  - POST /tickets/{id}/messages: Add a message to a support ticket conversation
 */
@RestController
@RequestMapping("/tickets")
public class SupportTicketController {

    @Autowired
    private SupportTicketService ticketService;

    public SupportTicketController(SupportTicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<SupportTicketResponse> createTicket(@RequestBody SupportTicketRequest request) {
        return ResponseEntity.ok(ticketService.createSupportTicket(request));
    }

    /**
     * Retrieve all support tickets in the system.
     *
     * @return List of all support tickets
     */
    @GetMapping("/all")
    public ResponseEntity<List<SupportTicketResponse>> getAllTicket() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    /**
     * Retrieve a specific support ticket by its ID.
     *
     * @param id Ticket ID
     * @return Support ticket details
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupportTicketResponse> getTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketByTicketId(id));
    }

    /**
     * Retrieve all support tickets associated with a specific user.
     *
     * @param id User ID
     * @return List of support tickets belonging to the user
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<List<SupportTicketResponse>> getTicketsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketsByUserId(id));
    }

    /**
     * Update the status or details of an existing support ticket.
     *
     * @param id Ticket ID
     * @param supportTicketUpdate Request containing updated status or notes
     * @return Updated support ticket details
     */
    @PatchMapping("/{id}")
    public ResponseEntity<SupportTicketResponse> updateTicket(
            @PathVariable long id,
            @RequestBody SupportTicketUpdate supportTicketUpdate) {
        return ResponseEntity.ok(ticketService.updateSupportTicket(id, supportTicketUpdate));
    }

    /**
     * Add a new message to an existing support ticket.
     *
     * @param id Ticket ID
     * @param request Request containing message details
     * @return Added message response with timestamp and sender info
     */
    @PostMapping("/{id}/messages")
    public ResponseEntity<MessageResponse> addMessage(
            @PathVariable Long id,
            @RequestBody MessageRequest request) {
        return ResponseEntity.ok(ticketService.addMessage(id, request));
    }
}
