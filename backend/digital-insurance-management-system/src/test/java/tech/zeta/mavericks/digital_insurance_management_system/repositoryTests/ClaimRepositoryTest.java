package tech.zeta.mavericks.digital_insurance_management_system.repositoryTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Claim;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Policy;
import tech.zeta.mavericks.digital_insurance_management_system.entity.User;
import tech.zeta.mavericks.digital_insurance_management_system.entity.UserPolicy;
import tech.zeta.mavericks.digital_insurance_management_system.enums.ClaimStatus;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyStatus;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyType;
import tech.zeta.mavericks.digital_insurance_management_system.repository.ClaimRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClaimRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClaimRepository claimRepository;

    // Entities
    private User user1;
    private User user2;
    private Policy policy;
    private UserPolicy userPolicy1;
    private UserPolicy userPolicy2;
    private Claim claim1;
    private Claim claim2;
    private Claim claim3;

    @BeforeEach
    void setup() {
        setupAndPersistTestEntities();
    }

    private void setupAndPersistTestEntities() {
        // User 1
        user1 = new User();
        user1.setName("John Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("hashedPassword123"); // correct field name
        user1.setPhone("1234567890");
        user1.setAddress("123 Main St, City, State");
        user1 = entityManager.persistAndFlush(user1);

        // User 2
        user2 = new User();
        user2.setName("Jane Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setPassword("hashedPassword456");
        user2.setPhone("0987654321");
        user2.setAddress("456 Oak Ave, City, State");
        user2 = entityManager.persistAndFlush(user2);

        // Policy (only fields that exist)
        policy = new Policy();
        policy.setName("Health Insurance Premium");
        policy.setDescription("Comprehensive health coverage");
        policy.setCoverageAmt(50000.0); // Double field
        policy.setDurationMonths(12);
        policy.setType(PolicyType.HEALTH);
        policy = entityManager.persistAndFlush(policy);

        // UserPolicy 1
        userPolicy1 = new UserPolicy();
        userPolicy1.setUser(user1);
        userPolicy1.setPolicy(policy);
        userPolicy1.setStartDate(LocalDate.now().minusMonths(6));
        userPolicy1.setEndDate(LocalDate.now().plusMonths(6));
        userPolicy1.setStatus(PolicyStatus.ACTIVE);
        userPolicy1.setPremiumPaid(1500.0); // Double
        userPolicy1 = entityManager.persistAndFlush(userPolicy1);

        // UserPolicy 2
        userPolicy2 = new UserPolicy();
        userPolicy2.setUser(user2);
        userPolicy2.setPolicy(policy);
        userPolicy2.setStartDate(LocalDate.now().minusMonths(3));
        userPolicy2.setEndDate(LocalDate.now().plusMonths(9));
        userPolicy2.setStatus(PolicyStatus.ACTIVE);
        userPolicy2.setPremiumPaid(1500.0);
        userPolicy2 = entityManager.persistAndFlush(userPolicy2);

        // Claim 1
        claim1 = new Claim();
        claim1.setUserPolicy(userPolicy1);
        claim1.setClaimDate(LocalDate.now().minusDays(10));
        claim1.setClaimAmount(new BigDecimal("5000.00"));
        claim1.setReason("Medical emergency treatment");
        claim1.setStatus(ClaimStatus.PENDING);
        claim1.setReviewerComment("");
        claim1 = entityManager.persistAndFlush(claim1);

        // Claim 2
        claim2 = new Claim();
        claim2.setUserPolicy(userPolicy1);
        claim2.setClaimDate(LocalDate.now().minusDays(5));
        claim2.setClaimAmount(new BigDecimal("3000.00"));
        claim2.setReason("Dental treatment");
        claim2.setStatus(ClaimStatus.APPROVED);
        claim2.setReviewerComment("Approved after verification");
        claim2.setResolvedDate(LocalDate.now().minusDays(2));
        claim2 = entityManager.persistAndFlush(claim2);

        // Claim 3
        claim3 = new Claim();
        claim3.setUserPolicy(userPolicy2);
        claim3.setClaimDate(LocalDate.now().minusDays(7));
        claim3.setClaimAmount(new BigDecimal("2500.00"));
        claim3.setReason("Eye surgery");
        claim3.setStatus(ClaimStatus.REJECTED);
        claim3.setReviewerComment("Insufficient documentation");
        claim3.setResolvedDate(LocalDate.now().minusDays(3));
        claim3 = entityManager.persistAndFlush(claim3);

        entityManager.clear();
    }

    @Test
    void findByUserPolicy_User_Id_ShouldReturnUserClaims_WhenValidUserId() {
        List<Claim> user1Claims = claimRepository.findByUserPolicy_User_Id(user1.getId());
        assertNotNull(user1Claims);
        assertEquals(2, user1Claims.size());
        user1Claims.forEach(c -> assertEquals(user1.getId(), c.getUserPolicy().getUser().getId()));
        assertTrue(user1Claims.stream().anyMatch(c -> c.getReason().equals("Medical emergency treatment")));
        assertTrue(user1Claims.stream().anyMatch(c -> c.getReason().equals("Dental treatment")));
    }

    @Test
    void findByUserPolicy_User_Id_ShouldReturnSingleClaim_WhenUserHasOneClaim() {
        List<Claim> user2Claims = claimRepository.findByUserPolicy_User_Id(user2.getId());
        assertNotNull(user2Claims);
        assertEquals(1, user2Claims.size());
        Claim c = user2Claims.get(0);
        assertEquals(user2.getId(), c.getUserPolicy().getUser().getId());
        assertEquals("Eye surgery", c.getReason());
        assertEquals(ClaimStatus.REJECTED, c.getStatus());
    }

    @Test
    void findByUserPolicy_User_Id_ShouldReturnEmptyList_WhenUserHasNoClaims() {
        User u = new User();
        u.setName("No Claims User");
        u.setEmail("noclaims@example.com");
        u.setPassword("password");
        u.setPhone("1111111111");
        u.setAddress("No Claims Address");
        u = entityManager.persistAndFlush(u);
        List<Claim> none = claimRepository.findByUserPolicy_User_Id(u.getId());
        assertNotNull(none);
        assertTrue(none.isEmpty());
    }

    @Test
    void findByUserPolicy_User_Id_ShouldReturnEmptyList_WhenInvalidUserId() {
        List<Claim> none = claimRepository.findByUserPolicy_User_Id(999L);
        assertNotNull(none);
        assertTrue(none.isEmpty());
    }

    @Test
    void findByUserPolicy_Id_ShouldReturnPolicyClaims_WhenValidPolicyId() {
        List<Claim> claims = claimRepository.findByUserPolicy_Id(userPolicy1.getId());
        assertNotNull(claims);
        assertEquals(2, claims.size());
        claims.forEach(c -> assertEquals(userPolicy1.getId(), c.getUserPolicy().getId()));
        assertTrue(claims.stream().anyMatch(c -> c.getClaimAmount().equals(new BigDecimal("5000.00"))));
        assertTrue(claims.stream().anyMatch(c -> c.getClaimAmount().equals(new BigDecimal("3000.00"))));
    }

    @Test
    void findByUserPolicy_Id_ShouldReturnSingleClaim_WhenPolicyHasOneClaim() {
        List<Claim> claims = claimRepository.findByUserPolicy_Id(userPolicy2.getId());
        assertNotNull(claims);
        assertEquals(1, claims.size());
        Claim c = claims.get(0);
        assertEquals(userPolicy2.getId(), c.getUserPolicy().getId());
        assertEquals(new BigDecimal("2500.00"), c.getClaimAmount());
        assertEquals(ClaimStatus.REJECTED, c.getStatus());
    }

    @Test
    void findByUserPolicy_Id_ShouldReturnEmptyList_WhenPolicyHasNoClaims() {
        User u = new User();
        u.setName("Policy No Claims User");
        u.setEmail("policynoclaims@example.com");
        u.setPassword("password");
        u.setPhone("2222222222");
        u.setAddress("Policy Address");
        u = entityManager.persistAndFlush(u);

        UserPolicy up = new UserPolicy();
        up.setUser(u);
        up.setPolicy(policy);
        up.setStartDate(LocalDate.now());
        up.setEndDate(LocalDate.now().plusMonths(12));
        up.setStatus(PolicyStatus.ACTIVE);
        up.setPremiumPaid(1500.0);
        up = entityManager.persistAndFlush(up);

        List<Claim> none = claimRepository.findByUserPolicy_Id(up.getId());
        assertNotNull(none);
        assertTrue(none.isEmpty());
    }

    @Test
    void findByUserPolicy_Id_ShouldReturnEmptyList_WhenInvalidPolicyId() {
        List<Claim> none = claimRepository.findByUserPolicy_Id(999L);
        assertNotNull(none);
        assertTrue(none.isEmpty());
    }

    @Test
    void findAll_ShouldReturnAllClaims() {
        List<Claim> claims = claimRepository.findAll();
        assertEquals(3, claims.size());
        assertTrue(claims.stream().anyMatch(c -> c.getStatus() == ClaimStatus.PENDING));
        assertTrue(claims.stream().anyMatch(c -> c.getStatus() == ClaimStatus.APPROVED));
        assertTrue(claims.stream().anyMatch(c -> c.getStatus() == ClaimStatus.REJECTED));
    }

    @Test
    void save_ShouldPersistClaim() {
        Claim c = new Claim();
        c.setUserPolicy(userPolicy1);
        c.setClaimDate(LocalDate.now());
        c.setClaimAmount(new BigDecimal("1000.00"));
        c.setReason("Prescription medication");
        c.setStatus(ClaimStatus.PENDING);
        c.setReviewerComment("");
        Claim saved = claimRepository.save(c);
        assertNotNull(saved.getId());
        entityManager.flush();
        Claim found = entityManager.find(Claim.class, saved.getId());
        assertEquals("Prescription medication", found.getReason());
    }

    @Test
    void findById_ShouldReturnClaim() {
        var opt = claimRepository.findById(claim1.getId());
        assertTrue(opt.isPresent());
        assertEquals(claim1.getId(), opt.get().getId());
    }

    @Test
    void findById_ShouldReturnEmpty() {
        assertTrue(claimRepository.findById(999L).isEmpty());
    }
}