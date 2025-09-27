package tech.zeta.mavericks.digital_insurance_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.MessageRequestDTO;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.SupportTicketRequestDTO;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.SupportTicketUpdateDTO;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.MessageResponseDTO;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.SupportTicketResponseDTO;
import tech.zeta.mavericks.digital_insurance_management_system.service.SupportTicketService;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class SupportTicketController {

    @Autowired
    private SupportTicketService ticketService;

    @PostMapping
    public ResponseEntity<SupportTicketResponseDTO> createTicket(@RequestBody SupportTicketRequestDTO request) {
        return ResponseEntity.ok(ticketService.createSupportTicket(request));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SupportTicketResponseDTO>> getAllTicket() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportTicketResponseDTO> getTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketByTicketId(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<SupportTicketResponseDTO>> getTicketsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketsByUserId(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SupportTicketResponseDTO> updateTicket(
            @PathVariable long id,
            @RequestBody SupportTicketUpdateDTO supportTicketUpdateDTO) {
        return ResponseEntity.ok(ticketService.updateSupportTicket(id, supportTicketUpdateDTO));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SupportTicketResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody SupportTicketUpdateDTO updateDTO) {
        return ResponseEntity.ok(ticketService.updateSupportTicket(id, updateDTO));
    }

    @PostMapping("/{id}/messages")
    public ResponseEntity<MessageResponseDTO> addMessage(
            @PathVariable Long id,
            @RequestBody MessageRequestDTO request) {
        return ResponseEntity.ok(ticketService.addMessage(id, request));
    }
}
