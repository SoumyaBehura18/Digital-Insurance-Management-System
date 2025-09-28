package tech.zeta.mavericks.digital_insurance_management_system.repositoryTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tech.zeta.mavericks.digital_insurance_management_system.entity.HealthPreexistingCondition;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Policy;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.repository.HealthPreexistingConditionRepository;
import tech.zeta.mavericks.digital_insurance_management_system.repository.PolicyRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HealthPreexistingConditionRepositoryTest {

    @Autowired
    private HealthPreexistingConditionRepository healthPreexistingConditionRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Test
    void testFindByPolicyId() {
        Policy policy = new Policy();
        policy.setName("Condition Policy");
        Policy savedPolicy = policyRepository.save(policy);

        HealthPreexistingCondition condition = new HealthPreexistingCondition();
        condition.setPolicy(savedPolicy);
        condition.setCondition(HealthCondition.CANCER);
        condition.setAdditionalPremium(1000.0);

        healthPreexistingConditionRepository.save(condition);

        List<HealthPreexistingCondition> results =
                healthPreexistingConditionRepository.findByPolicyId(savedPolicy.getId());

        assertEquals(1, results.size());
        assertEquals(HealthCondition.CANCER, results.get(0).getCondition());
    }
}
