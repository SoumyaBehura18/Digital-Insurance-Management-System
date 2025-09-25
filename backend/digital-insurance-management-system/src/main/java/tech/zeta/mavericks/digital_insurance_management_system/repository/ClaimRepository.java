package tech.zeta.mavericks.digital_insurance_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Claim;

//package tech.zeta.mavericks.digital_insurance_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.zeta.mavericks.digital_insurance_management_system.entity.Claim;

import java.util.List;


@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByUserPolicy_User_Id(Long userId);
}

//public interface ClaimRepository extends JpaRepository<Claim, Long> { }
