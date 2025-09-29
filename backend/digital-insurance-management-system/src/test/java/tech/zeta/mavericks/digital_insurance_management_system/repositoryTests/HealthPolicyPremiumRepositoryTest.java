package tech.zeta.mavericks.digital_insurance_management_system.repositoryTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tech.zeta.mavericks.digital_insurance_management_system.entity.HealthPolicyPremium;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Policy;
import tech.zeta.mavericks.digital_insurance_management_system.repository.HealthPolicyPremiumRepository;
import tech.zeta.mavericks.digital_insurance_management_system.repository.PolicyRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HealthPolicyPremiumRepositoryTest {

    @Autowired
    private HealthPolicyPremiumRepository healthPolicyPremiumRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Test
    void testFindFirstByPolicyId() {
        Policy policy = new Policy();
        policy.setName("Health Policy");
        Policy savedPolicy = policyRepository.save(policy);

        HealthPolicyPremium premium = new HealthPolicyPremium();
        premium.setPolicy(savedPolicy);
        premium.setPremiumRate(2000.0);
        premium.setRenewalRate(500.0);
        premium.setSmokingDrinking(false);

        healthPolicyPremiumRepository.save(premium);

        Optional<HealthPolicyPremium> result =
                healthPolicyPremiumRepository.findFirstByPolicyId(savedPolicy.getId());

        assertTrue(result.isPresent());
        assertEquals(2000.0, result.get().getPremiumRate());
    }
}
