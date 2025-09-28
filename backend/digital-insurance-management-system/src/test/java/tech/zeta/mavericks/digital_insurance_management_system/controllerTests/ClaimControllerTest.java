package tech.zeta.mavericks.digital_insurance_management_system.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.ClaimRequestDto;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.ClaimReviewDTO;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.ClaimListResponseDto;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.RemainingCoverageResponseDto;
import tech.zeta.mavericks.digital_insurance_management_system.controller.ClaimController;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Policy;
import tech.zeta.mavericks.digital_insurance_management_system.entity.User;
import tech.zeta.mavericks.digital_insurance_management_system.entity.UserPolicy;
import tech.zeta.mavericks.digital_insurance_management_system.enums.ClaimStatus;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyStatus;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyType;
import tech.zeta.mavericks.digital_insurance_management_system.service.ClaimService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClaimControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClaimService claimService;

    private ObjectMapper objectMapper;

    private ClaimRequestDto claimRequestDto;
    private ClaimListResponseDto claimListDto;
    private ClaimListResponseDto.ClaimResponseDto claimResponseDto;
    private ClaimReviewDTO claimReviewDTO;
    private UserPolicy userPolicy;
    private RemainingCoverageResponseDto remainingCoverageResponseDto;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ClaimController(claimService))
                .setControllerAdvice(new TestExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        initData();
    }

    private void initData() {
        claimRequestDto = new ClaimRequestDto();
        claimRequestDto.setUserPolicyId(1L);
        claimRequestDto.setClaimDate(LocalDate.now());
        claimRequestDto.setClaimAmount(new BigDecimal("5000.00"));
        claimRequestDto.setReason("Medical emergency treatment required");

        claimListDto = new ClaimListResponseDto();
        claimListDto.setId(1L);
        claimListDto.setUserPolicyId(1L);
        claimListDto.setUserId(10L);
        claimListDto.setUserName("John Doe");
        claimListDto.setUserEmail("john.doe@example.com");
        claimListDto.setPolicyName("Health Insurance Premium");
        claimListDto.setClaimDate(LocalDate.now());
        claimListDto.setClaimAmount(new BigDecimal("5000.00"));
        claimListDto.setReason("Medical emergency treatment required");
        claimListDto.setStatus(ClaimStatus.PENDING);

        claimResponseDto = new ClaimListResponseDto.ClaimResponseDto();
        claimResponseDto.setId(1L);
        claimResponseDto.setUserPolicyId(1L);
        claimResponseDto.setClaimDate(LocalDate.now());
        claimResponseDto.setClaimAmount(new BigDecimal("5000.00"));
        claimResponseDto.setReason("Medical emergency treatment required");
        claimResponseDto.setStatus(ClaimStatus.PENDING);

        claimReviewDTO = new ClaimReviewDTO();
        claimReviewDTO.setStatus(ClaimStatus.APPROVED);
        claimReviewDTO.setReviewComments("Docs verified");

        User user = new User();
        user.setId(10L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        Policy policy = new Policy();
        policy.setId(5L);
        policy.setName("Health Insurance Premium");
        policy.setType(PolicyType.HEALTH);

        userPolicy = new UserPolicy();
        userPolicy.setId(1L);
        userPolicy.setUser(user);
        userPolicy.setPolicy(policy);
        userPolicy.setStartDate(LocalDate.now().minusMonths(6));
        userPolicy.setEndDate(LocalDate.now().plusMonths(6));
        userPolicy.setStatus(PolicyStatus.ACTIVE);

        remainingCoverageResponseDto = new RemainingCoverageResponseDto(1L, 45000.0);
    }

    @Test
    void submitClaim_ShouldReturnCreatedDto() throws Exception {
        when(claimService.submitClaim(any())).thenReturn(claimResponseDto);
        mockMvc.perform(post("/claim")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(claimRequestDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.status").value("PENDING"))
            .andExpect(jsonPath("$.claimAmount").value(5000.00));
    }

    @Test
    void submitClaim_ShouldFailValidation() throws Exception {
        ClaimRequestDto invalid = new ClaimRequestDto();
        mockMvc.perform(post("/claim")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getAllClaims_ShouldReturnList() throws Exception {
        when(claimService.getAllClaimsDto()).thenReturn(Collections.singletonList(claimListDto));
        mockMvc.perform(get("/claim/claims"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].userName").value("John Doe"));
    }

    @Test
    void getAllClaims_ShouldReturnEmpty() throws Exception {
        when(claimService.getAllClaimsDto()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/claim/claims"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getClaimsByUserId_ShouldReturnList() throws Exception {
        when(claimService.getClaimsByUserIdDto(10L)).thenReturn(Collections.singletonList(claimListDto));
        mockMvc.perform(get("/claim/user/10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].userId").value(10L));
    }

    @Test
    void reviewClaim_ShouldReturnOk() throws Exception {
        mockMvc.perform(put("/claim/1/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(claimReviewDTO)))
            .andExpect(status().isOk());
    }

    @Test
    void reviewClaim_ShouldReturnError_WhenServiceThrows() throws Exception {
        doThrow(new RuntimeException("not found"))
            .when(claimService)
            .updateCalimStatusAndReviewerComment(eq(99L), eq(ClaimStatus.APPROVED), anyString());

        mockMvc.perform(put("/claim/99/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(claimReviewDTO)))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.error").value("not found"));
    }

    @Test
    void getPoliciesByUserId_ShouldReturnPolicies() throws Exception {
        when(claimService.getUserPoliciesByUserId(10L)).thenReturn(Collections.singletonList(userPolicy));
        mockMvc.perform(get("/claim/policy/10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }

    @Test
    void getRemainingCoverage_ShouldReturnValue() throws Exception {
        when(claimService.getRemainingCoverageAmount(1L)).thenReturn(remainingCoverageResponseDto);
        mockMvc.perform(get("/claim/policy/remaining-amount/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.policyId").value(1L))
            .andExpect(jsonPath("$.remainingClaimAmount").value(45000.0));
    }

    @Test
    void getAllPolicies_ShouldReturnAll() throws Exception {
        when(claimService.getAllUserPolicies()).thenReturn(Collections.singletonList(userPolicy));
        mockMvc.perform(get("/claim/policies"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L));
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
