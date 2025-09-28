package tech.zeta.mavericks.digital_insurance_management_system.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.PolicyRequest;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.PolicyWithPremiumDTO;
import tech.zeta.mavericks.digital_insurance_management_system.controller.PolicyController;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyType;
import tech.zeta.mavericks.digital_insurance_management_system.service.PolicyService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PolicyControllerTest {

    private MockMvc mockMvc;
    private PolicyController policyController;

    @Mock
    private PolicyService policyService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private PolicyWithPremiumDTO vehiclePolicyDTO;
    private PolicyWithPremiumDTO lifePolicyDTO;
    private PolicyWithPremiumDTO healthPolicyDTO;

    @BeforeEach
    void setup() {
        policyController = new PolicyController(policyService);
        mockMvc = MockMvcBuilders.standaloneSetup(policyController).build();

        vehiclePolicyDTO = new PolicyWithPremiumDTO(
                1L,
                "Car Insurance",
                PolicyType.VEHICLE,
                1000.0,
                900.0,
                12,
                50000.0
        );

        lifePolicyDTO = new PolicyWithPremiumDTO(
                2L,
                "Life Insurance",
                PolicyType.LIFE,
                2000.0,
                1800.0,
                120,
                100000.0
        );

        healthPolicyDTO = new PolicyWithPremiumDTO(
                3L,
                "Health Insurance",
                PolicyType.HEALTH,
                1500.0,
                1400.0,
                12,
                75000.0
        );
    }

    @Test
    void getPoliciesForUser_ShouldReturnAllPolicies() throws Exception {
        Mockito.when(policyService.getPoliciesForUser(any(PolicyRequest.class)))
                .thenReturn(List.of(vehiclePolicyDTO, lifePolicyDTO, healthPolicyDTO));

        PolicyRequest request = new PolicyRequest();
        request.setVehicleAge(2);
        request.setSmokingDrinking(true);

        mockMvc.perform(post("/policies/allPolicies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getVehiclePoliciesForUser_ShouldReturnVehiclePolicies() throws Exception {
        Mockito.when(policyService.getVehiclePoliciesForUser(any(PolicyRequest.class)))
                .thenReturn(List.of(vehiclePolicyDTO));

        PolicyRequest request = new PolicyRequest();
        request.setVehicleAge(2);

        mockMvc.perform(post("/policies/vehiclePolicies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getLifePoliciesForUser_ShouldReturnLifePolicies() throws Exception {
        Mockito.when(policyService.getLifePoliciesForUser(any(PolicyRequest.class)))
                .thenReturn(List.of(lifePolicyDTO));

        PolicyRequest request = new PolicyRequest();
        request.setSmokingDrinking(true);

        mockMvc.perform(post("/policies/lifePolicies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getHealthPoliciesForUser_ShouldReturnHealthPolicies() throws Exception {
        Mockito.when(policyService.getHealthPoliciesForUser(any(PolicyRequest.class)))
                .thenReturn(List.of(healthPolicyDTO));

        PolicyRequest request = new PolicyRequest();
        request.setSmokingDrinking(true);

        mockMvc.perform(post("/policies/healthPolicies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
