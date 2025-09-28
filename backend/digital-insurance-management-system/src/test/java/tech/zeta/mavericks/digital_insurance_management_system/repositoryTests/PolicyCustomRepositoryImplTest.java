package tech.zeta.mavericks.digital_insurance_management_system.repositoryTests;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.PolicyRequest;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.PolicyWithPremiumDTO;
import tech.zeta.mavericks.digital_insurance_management_system.entity.*;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyType;
import tech.zeta.mavericks.digital_insurance_management_system.enums.VehicleType;
import tech.zeta.mavericks.digital_insurance_management_system.repository.PolicyCustomRepositoryImpl;
import tech.zeta.mavericks.digital_insurance_management_system.repository.PolicyRepository;
import tech.zeta.mavericks.digital_insurance_management_system.repository.UserRepository;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PolicyCustomRepositoryImplTest {

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    private PolicyCustomRepositoryImpl policyCustomRepository;

    private Policy vehiclePolicy;
    private VehiclePolicyPremium vehiclePremium;

    private Policy lifePolicy;
    private LifePolicyPremium lifePremium;

    private Policy healthPolicy;
    private HealthPolicyPremium healthPremium;

    @BeforeEach
    void setup() {
        policyCustomRepository = new PolicyCustomRepositoryImpl();
        policyCustomRepository.em = em; // inject EntityManager manually

        // VEHICLE POLICY SETUP
        vehiclePolicy = new Policy();
        vehiclePolicy.setName("Car Insurance");
        vehiclePolicy.setType(PolicyType.VEHICLE);
        vehiclePolicy.setCoverageAmt(50000.0);
        vehiclePolicy.setDurationMonths(12);
        em.persist(vehiclePolicy);

        vehiclePremium = new VehiclePolicyPremium();
        vehiclePremium.setPolicy(vehiclePolicy);
        vehiclePremium.setVehicleAge(2);
        vehiclePremium.setPremiumRate(1000.0);
        vehiclePremium.setRenewalRate(900.0);
        em.persist(vehiclePremium);

        // LIFE POLICY SETUP
        lifePolicy = new Policy();
        lifePolicy.setName("Life Insurance");
        lifePolicy.setType(PolicyType.LIFE);
        lifePolicy.setCoverageAmt(100000.0);
        lifePolicy.setDurationMonths(120);
        em.persist(lifePolicy);

        lifePremium = new LifePolicyPremium();
        lifePremium.setPolicy(lifePolicy);
        lifePremium.setSmokingDrinking(true);
        lifePremium.setPremiumRate(2000.0);
        lifePremium.setRenewalRate(1800.0);
        em.persist(lifePremium);

        // HEALTH POLICY SETUP
        healthPolicy = new Policy();
        healthPolicy.setName("Health Insurance");
        healthPolicy.setType(PolicyType.HEALTH);
        healthPolicy.setCoverageAmt(75000.0);
        healthPolicy.setDurationMonths(12);
        em.persist(healthPolicy);

        healthPremium = new HealthPolicyPremium();
        healthPremium.setPolicy(healthPolicy);
        healthPremium.setPremiumRate(1500.0);
        healthPremium.setRenewalRate(1400.0);
        healthPremium.setSmokingDrinking(true);

        // Add preexisting condition
        HealthPreexistingCondition cond = new HealthPreexistingCondition();
        cond.setCondition(HealthCondition.DIABETES);
        cond.setAdditionalPremium(200.0);
        cond.setPolicy(healthPolicy);
        healthPremium.getPolicy().setPreexistingConditions(List.of(cond));

        em.persist(healthPremium);
        em.flush();
    }

    @Test
    void testFindVehiclePoliciesForUser() {
        PolicyRequest request = new PolicyRequest();
        request.setVehicleAge(2);

        List<PolicyWithPremiumDTO> results = policyCustomRepository.findVehiclePoliciesForUser(request);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getPolicyName()).isEqualTo("Car Insurance");
        assertThat(results.get(0).getPolicyType().toString()).isEqualTo("VEHICLE");

    }

    @Test
    void testFindLifePoliciesForUser() {
        PolicyRequest request = new PolicyRequest();
        request.setSmokingDrinking(true);

        List<PolicyWithPremiumDTO> results = policyCustomRepository.findLifePoliciesForUser(request);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getPolicyName()).isEqualTo("Life Insurance");
        assertThat(results.get(0).getPolicyType().toString()).isEqualTo("LIFE");

    }

    @Test
    void testFindHealthPoliciesForUser() {
        PolicyRequest request = new PolicyRequest();
        request.setSmokingDrinking(true);
        request.setPreexistingConditions(Set.of(HealthCondition.DIABETES));

        List<PolicyWithPremiumDTO> results = policyCustomRepository.findHealthPoliciesForUser(request);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getPolicyName()).isEqualTo("Health Insurance");
        assertThat(results.get(0).getPolicyType().toString()).isEqualTo("HEALTH");
        assertThat(results.get(0).getPremiumRate()).isEqualTo(1700.0); // 1500 base + 200 diabetes
    }

    @Test
    void testFindPoliciesForUser() {
        PolicyRequest request = new PolicyRequest();
        request.setVehicleAge(2);
        request.setSmokingDrinking(true);
        request.setPreexistingConditions(Set.of(HealthCondition.DIABETES));

        List<PolicyWithPremiumDTO> results = policyCustomRepository.findPoliciesForUser(request);
        assertThat(results).hasSize(3); // vehicle + life + health
    }
}
