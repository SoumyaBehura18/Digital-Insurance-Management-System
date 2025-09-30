package tech.zeta.mavericks.digital_insurance_management_system.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.UserUpdateRequest;
import tech.zeta.mavericks.digital_insurance_management_system.entity.User;
import tech.zeta.mavericks.digital_insurance_management_system.enums.RoleType;
import tech.zeta.mavericks.digital_insurance_management_system.exception.PolicyNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.exception.UserAlreadyExistException;
import tech.zeta.mavericks.digital_insurance_management_system.exception.UserNotFoundException;
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
        public User updateUserDetails(Long id, UserUpdateRequest request) {
            User userDetails = repo.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User Not Found"));

            // Map fields from request DTO to entity
            if (request.getName() != null) userDetails.setName(request.getName());
            if (request.getEmail() != null) userDetails.setEmail(request.getEmail());
            if (request.getAge() != null) userDetails.setAge(request.getAge());
            if (request.getPhone() != null) userDetails.setPhone(request.getPhone());
            if (request.getRoleType() != null) userDetails.setRoleType(request.getRoleType());
            if (request.getAddress() != null) userDetails.setAddress(request.getAddress());
            if (request.getSmokingDrinking() != null) userDetails.setSmokingDrinking(request.getSmokingDrinking());
            if (request.getPreexistingConditions() != null) userDetails.setPreexistingConditions(request.getPreexistingConditions());
            if (request.getVehicleType() != null) userDetails.setVehicleType(request.getVehicleType());
            if (request.getVehicleAge() != null) userDetails.setVehicleAge(request.getVehicleAge());

            return repo.save(userDetails);
        }

        public User getUserDetailsById(Long id){
        return repo.findById(id).orElseThrow(()-> new UserNotFoundException("User Not Found"));
        }

    public List<User> getUsersByIds(List<Long> ids) {
        return repo.findByIds(ids);
    }

}