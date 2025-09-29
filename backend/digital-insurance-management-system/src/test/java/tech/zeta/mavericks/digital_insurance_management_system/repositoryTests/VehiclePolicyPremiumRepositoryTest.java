package tech.zeta.mavericks.digital_insurance_management_system.repositoryTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tech.zeta.mavericks.digital_insurance_management_system.entity.VehiclePolicyPremium;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Policy;
import tech.zeta.mavericks.digital_insurance_management_system.repository.VehiclePolicyPremiumRepository;
import tech.zeta.mavericks.digital_insurance_management_system.repository.PolicyRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class VehiclePolicyPremiumRepositoryTest {

    @Autowired
    private VehiclePolicyPremiumRepository vehiclePolicyPremiumRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Test
    void testFindFirstByPolicyId() {
        Policy policy = new Policy();
        policy.setName("Vehicle Policy");
        Policy savedPolicy = policyRepository.save(policy);

        VehiclePolicyPremium premium = new VehiclePolicyPremium();
        premium.setPolicy(savedPolicy);
        premium.setPremiumRate(3000.0);
        premium.setRenewalRate(800.0);
        premium.setVehicleAge(5);

        vehiclePolicyPremiumRepository.save(premium);

        Optional<Object> result = vehiclePolicyPremiumRepository.findFirstByPolicyId(savedPolicy.getId());

        assertTrue(result.isPresent());
        VehiclePolicyPremium fetched = (VehiclePolicyPremium) result.get();
        assertEquals(3000.0, fetched.getPremiumRate());
    }
}
