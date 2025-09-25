package tech.zeta.mavericks.digital_insurance_management_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.MessageRequestDTO;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.SupportTicketRequestDTO;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.SupportTicketUpdateDTO;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.MessageResponseDTO;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.SupportTicketResponseDTO;
import tech.zeta.mavericks.digital_insurance_management_system.service.SupportTicketService;

@RestController
@RequestMapping("/tickets")
public class SupportTicketController {

    private SupportTicketService ticketService;

    @PostMapping
    public ResponseEntity<SupportTicketResponseDTO> createTicket(@RequestBody SupportTicketRequestDTO request) {
        return ResponseEntity.ok(ticketService.createSupportTicket(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportTicketResponseDTO> getTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketByTicketId(id));
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
