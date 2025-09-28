package tech.zeta.mavericks.digital_insurance_management_system.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.zeta.mavericks.digital_insurance_management_system.dto.premium.ConditionPremiumDTO;
import tech.zeta.mavericks.digital_insurance_management_system.dto.premium.HealthPremiumRequestDTO;
import tech.zeta.mavericks.digital_insurance_management_system.dto.premium.LifePremiumRequestDTO;
import tech.zeta.mavericks.digital_insurance_management_system.dto.premium.VehiclePremiumRequestDTO;

import tech.zeta.mavericks.digital_insurance_management_system.controller.PolicyAdminController;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Policy;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.service.PolicyServiceAdmin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PolicyAdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PolicyServiceAdmin policyServiceAdmin;

    @InjectMocks
    private PolicyAdminController policyAdminController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(policyAdminController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllPolicies() throws Exception {
        Policy p1 = new Policy();
        p1.setId(1L);
        p1.setName("Health Policy");

        Policy p2 = new Policy();
        p2.setId(2L);
        p2.setName("Life Policy");

//        when(policyServiceAdmin.getAllPolicies()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/admin/policies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Health Policy"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Life Policy"));
    }

    @Test
    void testCreatePolicy() throws Exception {
        Policy policy = new Policy();
        policy.setId(1L);
        policy.setName("Health Policy");

        when(policyServiceAdmin.createPolicy(any(Policy.class))).thenReturn(policy);

        mockMvc.perform(post("/api/admin/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(policy)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Health Policy"));
    }

    @Test
    void testAddHealthPremium() throws Exception {
        HealthPremiumRequestDTO request = new HealthPremiumRequestDTO();
        request.setPremiumRate(2000.0);
        request.setRenewalRate(300.0);

        Set<ConditionPremiumDTO> conditions = new HashSet<>();
        conditions.add(new ConditionPremiumDTO(HealthCondition.CANCER, 500.0)); // ✅ Works now
        request.setConditionPremiums(conditions);

        when(policyServiceAdmin.addHealthPremium(any(Long.class), any(), any(Double.class), any(Double.class)))
                .thenReturn(0.0);

        mockMvc.perform(post("/api/admin/policies/1/health-premium")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("0.0"));
    }
    @Test
    void testAddPreexistingCondition() throws Exception {
        HealthPremiumRequestDTO request = new HealthPremiumRequestDTO();
        Set<ConditionPremiumDTO> conditions = new HashSet<>();
        conditions.add(new ConditionPremiumDTO(HealthCondition.BP, 200.0));
        request.setConditionPremiums(conditions);

        when(policyServiceAdmin.addPreexistingCondition(any(Long.class), any())).thenReturn(0.0);

        mockMvc.perform(post("/api/admin/policies/1/preexisting-condition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("0.0"));
    }

    @Test
    void testAddLifePremium() throws Exception {
        LifePremiumRequestDTO request = new LifePremiumRequestDTO();
        request.setPremiumRate(5000.0);
        request.setRenewalRate(1000.0);

        when(policyServiceAdmin.addLifePremium(any(Long.class), any(Double.class), any(Double.class)))
                .thenReturn(0.0);

        mockMvc.perform(post("/api/admin/policies/1/life-premium")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("0.0"));
    }

    @Test
    void testAddVehiclePremium() throws Exception {
        VehiclePremiumRequestDTO request = new VehiclePremiumRequestDTO();
        request.setPremiumRate(3000.0);
        request.setRenewalRate(500.0);
        request.setVehicleAge(5);

        when(policyServiceAdmin.addVehiclePremium(any(Long.class), any(Double.class), any(Double.class), any(Integer.class)))
                .thenReturn(0.0);

        mockMvc.perform(post("/api/admin/policies/1/vehicle-premium")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("0.0"));
    }
}
