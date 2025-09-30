package tech.zeta.mavericks.digital_insurance_management_system.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.PolicyStatusRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.UserPolicyRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.UserPolicyResponse;
import tech.zeta.mavericks.digital_insurance_management_system.controller.UserPolicyController;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyStatus;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyType;
import tech.zeta.mavericks.digital_insurance_management_system.service.UserPolicyService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserPolicyControllerTest {

    private MockMvc mockMvc;
    private UserPolicyController userPolicyController;

    @Mock
    private UserPolicyService userPolicyService;

    private ObjectMapper objectMapper;

    private UserPolicyResponse response;

    @BeforeEach
    void setup() {
        userPolicyController = new UserPolicyController();
        // manually inject the mock service
        userPolicyController.userPolicyService = userPolicyService;

        mockMvc = MockMvcBuilders.standaloneSetup(userPolicyController).build();

        // ObjectMapper with JavaTimeModule to handle LocalDate
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Sample response
        response = new UserPolicyResponse(
                1L,
                101L,
                201L,
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                PolicyStatus.ACTIVE.name(),
                5000.0,
                "Health Policy",
                PolicyType.HEALTH,
                false,
                10000.0
        );
    }

    @Test
    void saveUserPolicy_ShouldReturnCreatedPolicy() throws Exception {
        UserPolicyRequest request = new UserPolicyRequest();
        request.setUserId(101L);
        request.setPolicyId(201L);
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusYears(1));
        request.setStatus(PolicyStatus.ACTIVE.name());
        request.setPremiumPaid(5000.0);

        Mockito.when(userPolicyService.saveUserPolicy(any(UserPolicyRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/user/policy/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(101))
                .andExpect(jsonPath("$.policyName").value("Health Policy"));
    }

    @Test
    void getUserPoliciesByUserId_ShouldReturnPolicies() throws Exception {
        Mockito.when(userPolicyService.getUserPoliciesByUserId(101L))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/user/policies/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(101));
    }

    @Test
    void getUserPolicyById_ShouldReturnPolicy() throws Exception {
        Mockito.when(userPolicyService.getUserPolicyById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/user/policy/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.policyName").value("Health Policy"));
    }

    @Test
    void updateUserPolicyById_ShouldReturnUpdatedPolicy() throws Exception {
        Mockito.when(userPolicyService.updateUserPolicyById(1L))
                .thenReturn(response);

        mockMvc.perform(patch("/user/policy/ncb/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.policyName").value("Health Policy"));
    }

    @Test
    void updateUserPolicyStatusById_ShouldReturnUpdatedPolicy() throws Exception {
        PolicyStatusRequest request = new PolicyStatusRequest();
        request.setPolicyStatus(PolicyStatus.EXPIRED);
        request.setPremiumRate(6000.0);

        Mockito.when(userPolicyService.updateUserPolicyStatusById(eq(1L), eq(PolicyStatus.EXPIRED), eq(6000.0)))
                .thenReturn(response);

        mockMvc.perform(patch("/user/policy/status/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.policyName").value("Health Policy"));
    }
}
