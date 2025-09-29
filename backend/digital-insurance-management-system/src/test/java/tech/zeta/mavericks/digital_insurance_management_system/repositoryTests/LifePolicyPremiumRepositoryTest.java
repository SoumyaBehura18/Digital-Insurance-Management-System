package tech.zeta.mavericks.digital_insurance_management_system.repositoryTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tech.zeta.mavericks.digital_insurance_management_system.entity.LifePolicyPremium;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Policy;
import tech.zeta.mavericks.digital_insurance_management_system.repository.LifePolicyPremiumRepository;
import tech.zeta.mavericks.digital_insurance_management_system.repository.PolicyRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LifePolicyPremiumRepositoryTest {

    @Autowired
    private LifePolicyPremiumRepository lifePolicyPremiumRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Test
    void testFindFirstByPolicyId() {
        Policy policy = new Policy();
        policy.setName("Life Policy");
        Policy savedPolicy = policyRepository.save(policy);

        LifePolicyPremium premium = new LifePolicyPremium();
        premium.setPolicy(savedPolicy);
        premium.setPremiumRate(5000.0);
        premium.setRenewalRate(1500.0);
        premium.setSmokingDrinking(true);

        lifePolicyPremiumRepository.save(premium);

        Optional<Object> result = lifePolicyPremiumRepository.findFirstByPolicyId(savedPolicy.getId());

        assertTrue(result.isPresent());
        LifePolicyPremium fetched = (LifePolicyPremium) result.get();
        assertEquals(5000.0, fetched.getPremiumRate());
    }
}
