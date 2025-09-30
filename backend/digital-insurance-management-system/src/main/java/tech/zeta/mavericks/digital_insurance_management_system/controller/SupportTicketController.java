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

    @GetMapping("/all")
    public ResponseEntity<List<SupportTicketResponse>> getAllTicket() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportTicketResponse> getTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketByTicketId(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<SupportTicketResponse>> getTicketsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketsByUserId(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SupportTicketResponse> updateTicket(
            @PathVariable long id,
            @RequestBody SupportTicketUpdate supportTicketUpdate) {
        return ResponseEntity.ok(ticketService.updateSupportTicket(id, supportTicketUpdate));
    }

    @PostMapping("/{id}/messages")
    public ResponseEntity<MessageResponse> addMessage(
            @PathVariable Long id,
            @RequestBody MessageRequest request) {
        return ResponseEntity.ok(ticketService.addMessage(id, request));
    }
}
