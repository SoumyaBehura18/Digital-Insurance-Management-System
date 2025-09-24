package tech.zeta.mavericks.digital_insurance_management_system.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tech.zeta.mavericks.digital_insurance_management_system.model.Users;
import tech.zeta.mavericks.digital_insurance_management_system.repo.UserRepo;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepo repo;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users user) {
        user.setUsername(user.getEmail());  // ← Add this
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
        return user;
    }

}