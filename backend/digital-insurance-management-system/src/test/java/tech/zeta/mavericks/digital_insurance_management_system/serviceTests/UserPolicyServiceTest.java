package tech.zeta.mavericks.digital_insurance_management_system.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.UserPolicyRequest;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.UserPolicyResponse;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPolicyServiceTest {

    @InjectMocks
    private UserPolicyService userPolicyService;

    @Mock
    private UserPolicyRepository userPolicyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PolicyRepository policyRepository;

    private User user;
    private Policy policy;
    private UserPolicy userPolicy;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");

        policy = new Policy();
        policy.setId(1L);
        policy.setName("Health Policy");
        policy.setType(PolicyType.valueOf("HEALTH"));
        policy.setCoverageAmt(10000.0);

        userPolicy = new UserPolicy();
        userPolicy.setId(1L);
        userPolicy.setUser(user);
        userPolicy.setPolicy(policy);
        userPolicy.setStartDate(LocalDate.now());
        userPolicy.setEndDate(LocalDate.now().plusYears(1));
        userPolicy.setStatus(PolicyStatus.ACTIVE);
        userPolicy.setPremiumPaid(5000.0);
        userPolicy.setNoClaimBonus(false);
    }

    @Test
    void saveUserPolicy_ShouldSaveSuccessfully() {
        UserPolicyRequest request = new UserPolicyRequest();
        request.setUserId(user.getId());
        request.setPolicyId(policy.getId());
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusYears(1));
        request.setStatus("ACTIVE");
        request.setPremiumPaid(5000.0);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(policyRepository.findById(policy.getId())).thenReturn(Optional.of(policy));
        when(userPolicyRepository.save(any(UserPolicy.class))).thenReturn(userPolicy);

        UserPolicyResponse response = userPolicyService.saveUserPolicy(request);

        assertNotNull(response);
        assertEquals(user.getId(), response.getUserId());
        assertEquals(policy.getId(), response.getPolicyId());
        assertEquals("ACTIVE", response.getStatus());
        verify(userPolicyRepository, times(1)).save(any(UserPolicy.class));
    }

    @Test
    void saveUserPolicy_ShouldThrowUserNotFoundException() {
        UserPolicyRequest request = new UserPolicyRequest();
        request.setUserId(2L);

        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userPolicyService.saveUserPolicy(request));
    }

    @Test
    void saveUserPolicy_ShouldThrowPolicyNotFoundException() {
        UserPolicyRequest request = new UserPolicyRequest();
        request.setUserId(user.getId());
        request.setPolicyId(2L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(policyRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(PolicyNotFoundException.class, () -> userPolicyService.saveUserPolicy(request));
    }

    @Test
    void getUserPoliciesByUserId_ShouldReturnPolicies() {
        when(userPolicyRepository.findByUserId(user.getId())).thenReturn(List.of(userPolicy));

        List<UserPolicyResponse> responses = userPolicyService.getUserPoliciesByUserId(user.getId());

        assertEquals(1, responses.size());
        assertEquals(user.getId(), responses.get(0).getUserId());
    }

    @Test
    void getUserPoliciesByUserId_ShouldThrowPolicyNotFoundException() {
        when(userPolicyRepository.findByUserId(user.getId())).thenReturn(Collections.emptyList());

        assertThrows(PolicyNotFoundException.class, () -> userPolicyService.getUserPoliciesByUserId(user.getId()));
    }

    @Test
    void getUserPolicyById_ShouldReturnPolicy() {
        when(userPolicyRepository.findById(userPolicy.getId())).thenReturn(Optional.of(userPolicy));

        UserPolicyResponse response = userPolicyService.getUserPolicyById(userPolicy.getId());

        assertNotNull(response);
        assertEquals(userPolicy.getId(), response.getId());
    }

    @Test
    void updateUserPolicyById_ShouldSetNoClaimBonusTrue() {
        when(userPolicyRepository.findById(userPolicy.getId())).thenReturn(Optional.of(userPolicy));
        when(userPolicyRepository.save(any(UserPolicy.class))).thenReturn(userPolicy);

        UserPolicyResponse response = userPolicyService.updateUserPolicyById(userPolicy.getId());

        assertTrue(response.getNoClaimBonus());
        verify(userPolicyRepository, times(1)).save(any(UserPolicy.class));
    }

    @Test
    void updateUserPolicyStatusById_ShouldUpdateStatusAndPremium() {
        when(userPolicyRepository.findById(userPolicy.getId())).thenReturn(Optional.of(userPolicy));
        when(userPolicyRepository.save(any(UserPolicy.class))).thenReturn(userPolicy);

        UserPolicyResponse response = userPolicyService.updateUserPolicyStatusById(
                userPolicy.getId(),
                PolicyStatus.EXPIRED,
                0.0
        );

        assertEquals("EXPIRED", response.getStatus());
        assertEquals(0.0, response.getPremiumPaid());
        verify(userPolicyRepository, times(1)).save(any(UserPolicy.class));
    }
}
