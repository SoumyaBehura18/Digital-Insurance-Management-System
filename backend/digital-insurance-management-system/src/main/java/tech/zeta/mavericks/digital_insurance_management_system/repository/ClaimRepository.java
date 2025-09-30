package tech.zeta.mavericks.digital_insurance_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Claim;

//package tech.zeta.mavericks.digital_insurance_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Claim;

import java.util.List;

/**
 * Repository interface for Claim entity operations.
 * Provides database access methods for claim management using Spring Data JPA.
 *
 * Extends JpaRepository to inherit standard CRUD operations and adds
 * custom query methods for claim-specific business requirements.
 *
 * @author Team Mavericks
 */
@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {

    /**
     * Finds all claims submitted by a specific user.
     * Uses property navigation to traverse UserPolicy -> User relationship.
     *
     * @param userId ID of the user whose claims to retrieve
     * @return List of claims belonging to the specified user
     */
    List<Claim> findByUserPolicy_User_Id(Long userId);

    /**
     * Finds all claims associated with a specific user policy instance.
     * Useful for viewing claim history for a particular policy purchase.
     *
     * @param userPolicyId ID of the user policy to search claims for
     * @return List of claims for the specified user policy
     */
    List<Claim> findByUserPolicy_Id(Long userPolicyId);
}

//public interface ClaimRepository extends JpaRepository<Claim, Long> { }
