package tech.zeta.mavericks.digital_insurance_management_system.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.zeta.mavericks.digital_insurance_management_system.model.Users;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {

    Users findByEmail(String email);
}