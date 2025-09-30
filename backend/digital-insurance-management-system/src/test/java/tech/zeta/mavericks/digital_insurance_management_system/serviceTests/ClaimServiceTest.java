package tech.zeta.mavericks.digital_insurance_management_system.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.ClaimRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.ClaimListResponse;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.RemainingCoverageResponse;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Claim;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Policy;
import tech.zeta.mavericks.digital_insurance_management_system.entity.User;
import tech.zeta.mavericks.digital_insurance_management_system.entity.UserPolicy;
import tech.zeta.mavericks.digital_insurance_management_system.enums.ClaimStatus;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyStatus;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyType;
import tech.zeta.mavericks.digital_insurance_management_system.exception.ClaimNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.exception.UserPolicyNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.repository.ClaimRepository;
import tech.zeta.mavericks.digital_insurance_management_system.repository.UserPolicyRepository;
import tech.zeta.mavericks.digital_insurance_management_system.service.ClaimService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaimServiceTest {

    @InjectMocks
    private ClaimService claimService;
    @Mock private ClaimRepository claimRepository;
    @Mock private UserPolicyRepository userPolicyRepository;

    private User user;
    private Policy policy;
    private UserPolicy userPolicy;
    private Claim claim;
    private ClaimRequest claimRequest;

    @BeforeEach
    void setup() { setupEntities(); setupDto(); }

    private void setupEntities() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        policy = new Policy();
        policy.setId(1L);
        policy.setName("Health Insurance Premium");
        policy.setDescription("Comprehensive health coverage");
        policy.setCoverageAmt(50000.0);
        policy.setDurationMonths(12);
        policy.setType(PolicyType.HEALTH);

        userPolicy = new UserPolicy();
        userPolicy.setId(1L);
        userPolicy.setUser(user);
        userPolicy.setPolicy(policy);
        userPolicy.setStartDate(LocalDate.now().minusMonths(6));
        userPolicy.setEndDate(LocalDate.now().plusMonths(6));
        userPolicy.setStatus(PolicyStatus.ACTIVE);
        userPolicy.setPremiumPaid(1500.0);

        claim = new Claim();
        claim.setId(1L);
        claim.setUserPolicy(userPolicy);
        claim.setClaimDate(LocalDate.now());
        claim.setClaimAmount(new BigDecimal("5000.00"));
        claim.setReason("Medical emergency treatment required");
        claim.setStatus(ClaimStatus.PENDING);
        claim.setReviewerComment("");
    }

    private void setupDto() {
        claimRequest = new ClaimRequest();
        claimRequest.setUserPolicyId(1L);
        claimRequest.setClaimDate(LocalDate.now());
        claimRequest.setClaimAmount(new BigDecimal("5000.00"));
        claimRequest.setReason("Medical emergency treatment required");
    }

    @Test
    void submitClaim_ShouldCreate_WhenActivePolicy() {
        when(userPolicyRepository.findById(1L)).thenReturn(Optional.of(userPolicy));
        when(claimRepository.save(any(Claim.class))).thenAnswer(inv -> { Claim c = inv.getArgument(0); c.setId(1L); return c; });
        ClaimListResponse.ClaimResponseDto dto = claimService.submitClaim(claimRequest);
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(ClaimStatus.PENDING, dto.getStatus());
        verify(claimRepository).save(any(Claim.class));
    }

    @Test
    void submitClaim_ShouldThrow_WhenUserPolicyMissing() {
        when(userPolicyRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserPolicyNotFoundException.class, () -> claimService.submitClaim(claimRequest));
        verify(claimRepository, never()).save(any());
    }

    @Test
    void submitClaim_ShouldThrow_WhenPolicyNotActive() {
        userPolicy.setStatus(PolicyStatus.EXPIRED);
        when(userPolicyRepository.findById(1L)).thenReturn(Optional.of(userPolicy));
        assertThrows(RuntimeException.class, () -> claimService.submitClaim(claimRequest));
        verify(claimRepository, never()).save(any());
    }

    @Test
    void getAllClaimsDto_ShouldMap() {
        when(claimRepository.findAll()).thenReturn(Collections.singletonList(claim));
        List<ClaimListResponse> list = claimService.getAllClaimsDto();
        assertEquals(1, list.size());
        assertEquals("John Doe", list.get(0).getUserName());
    }

    @Test
    void getAllClaimsDto_Empty() {
        when(claimRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(claimService.getAllClaimsDto().isEmpty());
    }

    @Test
    void getClaimsByUserIdDto_ShouldReturn() {
        when(claimRepository.findByUserPolicy_User_Id(1L)).thenReturn(Collections.singletonList(claim));
        List<ClaimListResponse> list = claimService.getClaimsByUserIdDto(1L);
        assertEquals(1, list.size());
        assertEquals(1L, list.get(0).getUserPolicyId());
    }

    @Test
    void getClaimsByUserIdDto_Empty() {
        when(claimRepository.findByUserPolicy_User_Id(1L)).thenReturn(Collections.emptyList());
        assertTrue(claimService.getClaimsByUserIdDto(1L).isEmpty());
    }

    @Test
    void updateStatus_ShouldPersist() {
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));
        when(claimRepository.save(any(Claim.class))).thenAnswer(inv -> inv.getArgument(0));
        claimService.updateCalimStatusAndReviewerComment(1L, ClaimStatus.APPROVED, "Docs");
        assertEquals(ClaimStatus.APPROVED, claim.getStatus());
        assertEquals("Docs", claim.getReviewerComment());
        assertNotNull(claim.getResolvedDate());
    }

    @Test
    void updateStatus_ShouldThrow_WhenMissing() {
        when(claimRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ClaimNotFoundException.class, () -> claimService.updateCalimStatusAndReviewerComment(99L, ClaimStatus.REJECTED, "X"));
    }

    @Test
    void getUserPoliciesByUserId_ShouldReturn() {
        when(userPolicyRepository.findByUser_Id(1L)).thenReturn(Collections.singletonList(userPolicy));
        List<UserPolicy> result = claimService.getUserPoliciesByUserId(1L);
        assertEquals(1, result.size());
        assertEquals(PolicyStatus.ACTIVE, result.get(0).getStatus());
    }

    @Test
    void remainingCoverage_Calculates() {
        when(userPolicyRepository.findById(1L)).thenReturn(Optional.of(userPolicy));
        Claim approved = new Claim();
        approved.setUserPolicy(userPolicy);
        approved.setStatus(ClaimStatus.APPROVED);
        approved.setClaimAmount(new BigDecimal("5000"));
        when(claimRepository.findByUserPolicy_Id(1L)).thenReturn(Collections.singletonList(approved));
        RemainingCoverageResponse dto = claimService.getRemainingCoverageAmount(1L);
        assertEquals(45000.0, dto.getRemainingClaimAmount());
    }

    @Test
    void remainingCoverage_Throws_WhenMissingPolicy() {
        when(userPolicyRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserPolicyNotFoundException.class, () -> claimService.getRemainingCoverageAmount(1L));
    }

    @Test
    void allUserPolicies_ReturnsList() {
        when(userPolicyRepository.findAll()).thenReturn(Collections.singletonList(userPolicy));
        assertEquals(1, claimService.getAllUserPolicies().size());
    }

    @Test
    void allUserPolicies_Empty() {
        when(userPolicyRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(claimService.getAllUserPolicies().isEmpty());
    }
}
