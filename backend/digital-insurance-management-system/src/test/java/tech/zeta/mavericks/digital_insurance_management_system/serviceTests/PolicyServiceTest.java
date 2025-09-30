package tech.zeta.mavericks.digital_insurance_management_system.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.PolicyRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.PolicyWithPremium;
import tech.zeta.mavericks.digital_insurance_management_system.repository.PolicyRepository;
import tech.zeta.mavericks.digital_insurance_management_system.service.PolicyService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PolicyServiceTest {

    private PolicyRepository.PolicyCustomRepository policyRepo;
    private PolicyService policyService;

    @BeforeEach
    void setUp() {
        policyRepo = Mockito.mock(PolicyRepository.PolicyCustomRepository.class);
        policyService = new PolicyService(policyRepo);
    }

    @Test
    void testGetPoliciesForUser() {
        PolicyRequest mockRequest = new PolicyRequest();
        List<PolicyWithPremium> mockPolicies = List.of(new PolicyWithPremium(), new PolicyWithPremium());

        when(policyRepo.findPoliciesForUser(mockRequest)).thenReturn(mockPolicies);

        List<PolicyWithPremium> result = policyService.getPoliciesForUser(mockRequest);

        assertEquals(mockPolicies, result);
        verify(policyRepo, times(1)).findPoliciesForUser(mockRequest);
        verifyNoMoreInteractions(policyRepo);
    }

    @Test
    void testGetVehiclePoliciesForUser() {
        PolicyRequest mockRequest = new PolicyRequest();
        List<PolicyWithPremium> mockPolicies = List.of(new PolicyWithPremium());

        when(policyRepo.findVehiclePoliciesForUser(mockRequest)).thenReturn(mockPolicies);

        List<PolicyWithPremium> result = policyService.getVehiclePoliciesForUser(mockRequest);

        assertEquals(mockPolicies, result);
        verify(policyRepo, times(1)).findVehiclePoliciesForUser(mockRequest);
        verifyNoMoreInteractions(policyRepo);
    }

    @Test
    void testGetLifePoliciesForUser() {
        PolicyRequest mockRequest = new PolicyRequest();
        List<PolicyWithPremium> mockPolicies = List.of(new PolicyWithPremium());

        when(policyRepo.findLifePoliciesForUser(mockRequest)).thenReturn(mockPolicies);

        List<PolicyWithPremium> result = policyService.getLifePoliciesForUser(mockRequest);

        assertEquals(mockPolicies, result);
        verify(policyRepo, times(1)).findLifePoliciesForUser(mockRequest);
        verifyNoMoreInteractions(policyRepo);
    }

    @Test
    void testGetHealthPoliciesForUser() {
        PolicyRequest mockRequest = new PolicyRequest();
        List<PolicyWithPremium> mockPolicies = List.of(new PolicyWithPremium());

        when(policyRepo.findHealthPoliciesForUser(mockRequest)).thenReturn(mockPolicies);

        List<PolicyWithPremium> result = policyService.getHealthPoliciesForUser(mockRequest);

        assertEquals(mockPolicies, result);
        verify(policyRepo, times(1)).findHealthPoliciesForUser(mockRequest);
        verifyNoMoreInteractions(policyRepo);
    }
}
