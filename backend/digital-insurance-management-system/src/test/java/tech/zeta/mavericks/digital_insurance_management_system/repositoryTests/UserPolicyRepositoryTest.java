package tech.zeta.mavericks.digital_insurance_management_system.repositoryTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Policy;
import tech.zeta.mavericks.digital_insurance_management_system.entity.User;
import tech.zeta.mavericks.digital_insurance_management_system.entity.UserPolicy;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyStatus;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyType;
import tech.zeta.mavericks.digital_insurance_management_system.repository.PolicyRepository;
import tech.zeta.mavericks.digital_insurance_management_system.repository.UserPolicyRepository;
import tech.zeta.mavericks.digital_insurance_management_system.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserPolicyRepositoryTest {

    @Autowired
    private UserPolicyRepository userPolicyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PolicyRepository policyRepository;

    private User user;
    private Policy policy;
    private UserPolicy userPolicy;

    @BeforeEach
    void setup() {
        user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("hashedpwd");
        user.setPhone("1234567890");
        user.setAge(30);
        user = userRepository.save(user);

        policy = new Policy();
        policy.setName("Health Policy");
        policy.setType(PolicyType.valueOf("HEALTH"));
        policy.setCoverageAmt(10000.0);
        policy = policyRepository.save(policy);

        userPolicy = new UserPolicy();
        userPolicy.setUser(user);
        userPolicy.setPolicy(policy);
        userPolicy.setStartDate(LocalDate.now());
        userPolicy.setEndDate(LocalDate.now().plusYears(1));
        userPolicy.setStatus(PolicyStatus.ACTIVE);
        userPolicy.setPremiumPaid(5000.0);
        userPolicy.setNoClaimBonus(false);

        userPolicyRepository.save(userPolicy);
    }

    @Test
    void findByUser_Id_ShouldReturnPolicies() {
        List<UserPolicy> results = userPolicyRepository.findByUser_Id(user.getId());

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void findByUserId_ShouldReturnPolicies() {
        List<UserPolicy> results = userPolicyRepository.findByUserId(user.getId());

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void findByUserId_WhenNoPolicies_ShouldReturnEmptyList() {
        List<UserPolicy> results = userPolicyRepository.findByUserId(999L);

        assertThat(results).isEmpty();
    }
}
