package tech.zeta.mavericks.digital_insurance_management_system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.zeta.mavericks.digital_insurance_management_system.entity.UserPrincipal;
import tech.zeta.mavericks.digital_insurance_management_system.entity.User;
import tech.zeta.mavericks.digital_insurance_management_system.repository.UserRepository;

/**
 * Service class that implements UserDetailsService to load user-specific data
 * during authentication. It fetches user information from the database using email.
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

    @Autowired
    private UserRepository userRepo;

    /**
     * Locates the user based on the username (email) provided.
     *
     * @param username the email of the user trying to authenticate
     * @return UserDetails object containing user authentication information
     * @throws UsernameNotFoundException if the user is not found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user by username: {}", username);

        User user = userRepo.findByEmail(username);

        if (user == null) {
            logger.warn("User not found with email: {}", username);
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        logger.info("User found: {}", username);
        return new UserPrincipal(user);
    }
}
