package tech.zeta.mavericks.digital_insurance_management_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.zeta.mavericks.digital_insurance_management_system.dto.ClaimRequestDto;
import tech.zeta.mavericks.digital_insurance_management_system.dto.ClaimResponseDto;
import tech.zeta.mavericks.digital_insurance_management_system.service.ClaimService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/claim")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService claimService;

    @PostMapping
    public ResponseEntity<ClaimResponseDto> submitClaim(@Valid @RequestBody ClaimRequestDto claimRequestDto) {
        ClaimResponseDto response = claimService.submitClaim(claimRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
