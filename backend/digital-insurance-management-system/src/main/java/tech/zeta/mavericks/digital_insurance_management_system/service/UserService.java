package tech.zeta.mavericks.digital_insurance_management_system.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tech.zeta.mavericks.digital_insurance_management_system.entity.User;
import tech.zeta.mavericks.digital_insurance_management_system.enums.RoleType;
import tech.zeta.mavericks.digital_insurance_management_system.exception.PolicyNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.exception.UserAlreadyExistException;
import tech.zeta.mavericks.digital_insurance_management_system.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepository repo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User register(User user) {
        String email = user.getEmail();

        // Check if user already exists
        User existingUser = repo.findByEmail(email);
        if (existingUser != null) {
            throw new UserAlreadyExistException("User with email "+email+" already exist");
        }

        // Encode password
        System.out.println("Password: " + user.getPassword());
        user.setPassword(encoder.encode(user.getPassword()));

        // Save user
        return repo.save(user);
    }



    public List<User> getAllUsers(){
        return repo.findAll();
    }

    public User updateUserRole(Long id, RoleType roleType){
        System.out.println("Inside Here");
        User user=repo.findById(id).orElseThrow(()-> new PolicyNotFoundException("User Not Found"));
        user.setRoleType(roleType);
        return repo.save(user);

    }

    public List<User> getUsersByIds(List<Long> ids) {
        return repo.findByIds(ids);
    }

}