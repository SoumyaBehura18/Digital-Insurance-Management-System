package tech.zeta.mavericks.digital_insurance_management_system.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tech.zeta.mavericks.digital_insurance_management_system.dto.premium.ConditionPremiumDTO;
import tech.zeta.mavericks.digital_insurance_management_system.dto.premium.HealthPremiumRequestDTO;
import tech.zeta.mavericks.digital_insurance_management_system.entity.*;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.exception.PolicyNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.repository.*;
import tech.zeta.mavericks.digital_insurance_management_system.service.PolicyServiceAdmin;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PolicyServiceAdminTest {

    @Mock private PolicyRepository policyRepository;
    @Mock private HealthPolicyPremiumRepository healthRepo;
    @Mock private LifePolicyPremiumRepository lifeRepo;
    @Mock private VehiclePolicyPremiumRepository vehicleRepo;
    @Mock private HealthPreexistingConditionRepository healthPreCondRepo;

    @InjectMocks
    private PolicyServiceAdmin policyService;

    private Policy policy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        policy = new Policy();
        policy.setId(1L);
        policy.setName("Test Policy");
    }

    @Test
    void testCreatePolicy() {
        when(policyRepository.save(any(Policy.class))).thenReturn(policy);

        Policy result = policyService.createPolicy(policy);

        assertNotNull(result);
        assertEquals("Test Policy", result.getName());
        verify(policyRepository, times(1)).save(policy);
    }

    @Test
    void testAddHealthPremium_Success() {
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));

        Set<ConditionPremiumDTO> conditions = new HashSet<ConditionPremiumDTO>();
        conditions.add(new ConditionPremiumDTO(HealthCondition.CANCER, 500.0));

        double total = policyService.addHealthPremium(1L, conditions, 2000.0, 300.0);

        assertEquals(0.0, total);
        verify(healthRepo, times(2)).save(any(HealthPolicyPremium.class)); // smoker + non-smoker
        verify(healthPreCondRepo, times(1)).save(any(HealthPreexistingCondition.class));
    }

    @Test
    void testAddHealthPremium_PolicyNotFound() {
        when(policyRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(PolicyNotFoundException.class, () ->
                policyService.addHealthPremium(2L, new HashSet<ConditionPremiumDTO>(), 2000.0, 300.0)
        );
    }

    @Test
    void testAddPreexistingCondition() {
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));

        Set<ConditionPremiumDTO> conditions = new HashSet<>();
        conditions.add(new ConditionPremiumDTO(HealthCondition.BP, 500.0)); // ✅ Enum constructor

        double result = policyService.addPreexistingCondition(1L, conditions);

        assertEquals(0.0, result);
        verify(healthPreCondRepo, times(1)).save(any(HealthPreexistingCondition.class));
    }

    @Test
    void testAddLifePremium() {
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));

        double result = policyService.addLifePremium(1L, 5000.0, 1000.0);

        assertEquals(0.0, result);
        verify(lifeRepo, times(2)).save(any(LifePolicyPremium.class)); // smoker + non-smoker
    }

    @Test
    void testAddVehiclePremium_withVehicleAge() {
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));

        double result = policyService.addVehiclePremium(1L, 3000.0, 500.0, 4);

        assertEquals(0.0, result);
        verify(vehicleRepo, times(1)).save(any(VehiclePolicyPremium.class));
    }

    @Test
    void testAddVehiclePremium_withNullVehicleAge() {
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));

        double result = policyService.addVehiclePremium(1L, 3000.0, 500.0, null);

        assertEquals(0.0, result);
        ArgumentCaptor<VehiclePolicyPremium> captor = ArgumentCaptor.forClass(VehiclePolicyPremium.class);
        verify(vehicleRepo, times(1)).save(captor.capture());

        VehiclePolicyPremium saved = captor.getValue();
        assertEquals(Integer.MAX_VALUE, saved.getVehicleAge());
    }

    @Test
    void testAddVehiclePremium_PolicyNotFound() {
        when(policyRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(PolicyNotFoundException.class, () -> {
            policyService.addVehiclePremium(2L, 3000.0, 500.0, 5);
        });
    }
}
