package tech.zeta.mavericks.digital_insurance_management_system.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.UserPolicyRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.UserPolicyResponse;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Policy;
import tech.zeta.mavericks.digital_insurance_management_system.entity.User;
import tech.zeta.mavericks.digital_insurance_management_system.entity.UserPolicy;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyStatus;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyType;
import tech.zeta.mavericks.digital_insurance_management_system.exception.PolicyNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.exception.UserNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.repository.PolicyRepository;
import tech.zeta.mavericks.digital_insurance_management_system.repository.UserPolicyRepository;
import tech.zeta.mavericks.digital_insurance_management_system.repository.UserRepository;
import tech.zeta.mavericks.digital_insurance_management_system.service.UserPolicyService;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPolicyServiceTest {

    @Mock
    private UserPolicyRepository userPolicyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PolicyRepository policyRepository;

    @InjectMocks
    private UserPolicyService userPolicyService;

    private User user;
    private Policy policy;
    private UserPolicyRequest request;
    private UserPolicy userPolicy;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        policy = new Policy();
        policy.setId(1L);
        policy.setName("Health Policy");
        policy.setType(PolicyType.HEALTH);
        policy.setCoverageAmt(100000.0);

        request = new UserPolicyRequest();
        request.setUserId(1L);
        request.setPolicyId(1L);
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusYears(1));
        request.setStatus("ACTIVE");
        request.setPremiumPaid(12000.0);

        userPolicy = new UserPolicy();
        userPolicy.setId(1L);
        userPolicy.setUser(user);
        userPolicy.setPolicy(policy);
        userPolicy.setStartDate(request.getStartDate());
        userPolicy.setEndDate(request.getEndDate());
        userPolicy.setStatus(PolicyStatus.ACTIVE);
        userPolicy.setPremiumPaid(12000.0);
        userPolicy.setNoClaimBonus(false);
    }

    @Test
    void testSaveUserPolicy_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        when(userPolicyRepository.save(any(UserPolicy.class))).thenReturn(userPolicy);

        UserPolicyResponse response = userPolicyService.saveUserPolicy(request);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals(1L, response.getPolicyId());
        assertEquals("Health Policy", response.getPolicyName());
        assertEquals(PolicyType.HEALTH, response.getPolicyType());
        assertEquals(100000.0, response.getCoverageAmount());
        assertEquals(12000.0, response.getPremiumPaid());
        assertEquals("ACTIVE", response.getStatus());
        assertFalse(response.getNoClaimBonus());
        verify(userPolicyRepository, times(1)).save(any(UserPolicy.class));
    }

    @Test
    void testSaveUserPolicy_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userPolicyService.saveUserPolicy(request));
        assertEquals("User not found", exception.getMessage());

        verify(policyRepository, never()).findById(any());
        verify(userPolicyRepository, never()).save(any());
    }

    @Test
    void testSaveUserPolicy_PolicyNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(policyRepository.findById(1L)).thenReturn(Optional.empty());

        PolicyNotFoundException exception = assertThrows(PolicyNotFoundException.class,
                () -> userPolicyService.saveUserPolicy(request));
        assertEquals("Policy not found", exception.getMessage());

        verify(userPolicyRepository, never()).save(any());
    }

    @Test
    void testGetUserPoliciesByUserId_Success() {
        List<UserPolicy> list = new ArrayList<>();
        list.add(userPolicy);

        when(userPolicyRepository.findByUserId(1L)).thenReturn(list);

        List<UserPolicyResponse> responses = userPolicyService.getUserPoliciesByUserId(1L);

        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getUserId());
        assertEquals("Health Policy", responses.get(0).getPolicyName());
        verify(userPolicyRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testGetUserPoliciesByUserId_NotFound() {
        when(userPolicyRepository.findByUserId(1L)).thenReturn(new ArrayList<>());

        PolicyNotFoundException exception = assertThrows(PolicyNotFoundException.class,
                () -> userPolicyService.getUserPoliciesByUserId(1L));
        assertEquals("No policies found for user with ID 1", exception.getMessage());
    }

    @Test
    void testGetUserPolicyById_Success() {
        when(userPolicyRepository.findById(1L)).thenReturn(Optional.of(userPolicy));

        UserPolicyResponse response = userPolicyService.getUserPolicyById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals(1L, response.getPolicyId());
        assertEquals("Health Policy", response.getPolicyName());
        verify(userPolicyRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserPolicyById_NotFound() {
        when(userPolicyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> userPolicyService.getUserPolicyById(1L));
        verify(userPolicyRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateUserPolicyById_Success() {
        when(userPolicyRepository.findById(1L)).thenReturn(Optional.of(userPolicy));

        // Create a copy of userPolicy with noClaimBonus set to true for the save return
        UserPolicy updatedUserPolicy = new UserPolicy();
        updatedUserPolicy.setId(1L);
        updatedUserPolicy.setUser(user);
        updatedUserPolicy.setPolicy(policy);
        updatedUserPolicy.setStartDate(userPolicy.getStartDate());
        updatedUserPolicy.setEndDate(userPolicy.getEndDate());
        updatedUserPolicy.setStatus(userPolicy.getStatus());
        updatedUserPolicy.setPremiumPaid(userPolicy.getPremiumPaid());
        updatedUserPolicy.setNoClaimBonus(true);

        when(userPolicyRepository.save(any(UserPolicy.class))).thenReturn(updatedUserPolicy);

        UserPolicyResponse response = userPolicyService.updateUserPolicyById(1L);

        assertNotNull(response);
        assertTrue(response.getNoClaimBonus());
        verify(userPolicyRepository, times(1)).findById(1L);
        verify(userPolicyRepository, times(1)).save(any(UserPolicy.class));
    }

    @Test
    void testUpdateUserPolicyById_NotFound() {
        when(userPolicyRepository.findById(1L)).thenReturn(Optional.empty());

        PolicyNotFoundException exception = assertThrows(PolicyNotFoundException.class,
                () -> userPolicyService.updateUserPolicyById(1L));
        assertEquals("UserPolicy not found with id: 1", exception.getMessage());

        verify(userPolicyRepository, never()).save(any());
    }

    @Test
    void testUpdateUserPolicyStatusById_Success() {
        when(userPolicyRepository.findById(1L)).thenReturn(Optional.of(userPolicy));

        // Create updated policy with new status and premium
        UserPolicy updatedUserPolicy = new UserPolicy();
        updatedUserPolicy.setId(1L);
        updatedUserPolicy.setUser(user);
        updatedUserPolicy.setPolicy(policy);
        updatedUserPolicy.setStartDate(userPolicy.getStartDate());
        updatedUserPolicy.setEndDate(userPolicy.getEndDate());
        updatedUserPolicy.setStatus(PolicyStatus.EXPIRED);
        updatedUserPolicy.setPremiumPaid(15000.0);
        updatedUserPolicy.setNoClaimBonus(false);

        when(userPolicyRepository.save(any(UserPolicy.class))).thenReturn(updatedUserPolicy);

        UserPolicyResponse response = userPolicyService.updateUserPolicyStatusById(1L, PolicyStatus.EXPIRED, 15000.0);

        assertNotNull(response);
        assertEquals(PolicyStatus.EXPIRED.name(), response.getStatus());
        assertEquals(15000.0, response.getPremiumPaid());
        verify(userPolicyRepository, times(1)).findById(1L);
        verify(userPolicyRepository, times(1)).save(any(UserPolicy.class));
    }

    @Test
    void testUpdateUserPolicyStatusById_NotFound() {
        when(userPolicyRepository.findById(1L)).thenReturn(Optional.empty());

        PolicyNotFoundException exception = assertThrows(PolicyNotFoundException.class,
                () -> userPolicyService.updateUserPolicyStatusById(1L, PolicyStatus.EXPIRED, 15000.0));
        assertEquals("UserPolicy not found with id: 1", exception.getMessage());

        verify(userPolicyRepository, never()).save(any());
    }

    @Test
    void testMapToResponse_AllFields() {
        // This test ensures the private mapToResponse method is fully covered
        // by testing through a public method that uses it
        when(userPolicyRepository.findById(1L)).thenReturn(Optional.of(userPolicy));

        UserPolicyResponse response = userPolicyService.getUserPolicyById(1L);

        // Verify all fields are properly mapped
        assertEquals(userPolicy.getId(), response.getId());
        assertEquals(userPolicy.getUser().getId(), response.getUserId());
        assertEquals(userPolicy.getPolicy().getId(), response.getPolicyId());
        assertEquals(userPolicy.getStartDate(), response.getStartDate());
        assertEquals(userPolicy.getEndDate(), response.getEndDate());
        assertEquals(userPolicy.getStatus().name(), response.getStatus());
        assertEquals(userPolicy.getPremiumPaid(), response.getPremiumPaid());
        assertEquals(userPolicy.getPolicy().getName(), response.getPolicyName());
        assertEquals(userPolicy.getPolicy().getType(), response.getPolicyType());
        assertEquals(userPolicy.getNoClaimBonus(), response.getNoClaimBonus());
        assertEquals(userPolicy.getPolicy().getCoverageAmt(), response.getCoverageAmount());
    }
}