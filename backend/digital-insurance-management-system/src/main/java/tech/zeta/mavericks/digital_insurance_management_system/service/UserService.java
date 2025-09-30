package tech.zeta.mavericks.digital_insurance_management_system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.UserUpdateRequest;
import tech.zeta.mavericks.digital_insurance_management_system.entity.User;
import tech.zeta.mavericks.digital_insurance_management_system.enums.RoleType;
import tech.zeta.mavericks.digital_insurance_management_system.exception.UserAlreadyExistException;
import tech.zeta.mavericks.digital_insurance_management_system.exception.UserNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.repository.UserRepository;

import java.util.List;

/**
 * Service class for managing users.
 * Handles registration, updating user details, roles, and fetching users.
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository repo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    /**
     * Registers a new user.
     *
     * @param user the user entity to register
     * @return the saved user
     * @throws UserAlreadyExistException if user with email already exists
     */
    public User register(User user) {
        String email = user.getEmail();
        logger.info("Registering user with email: {}", email);

        User existingUser = repo.findByEmail(email);
        if (existingUser != null) {
            logger.warn("User with email {} already exists", email);
            throw new UserAlreadyExistException("User with email " + email + " already exists");
        }

        logger.debug("Encoding password for user with email: {}", email);
        user.setPassword(encoder.encode(user.getPassword()));

        User savedUser = repo.save(user);
        logger.info("User registered successfully with ID: {}", savedUser.getId());
        return savedUser;
    }

    /**
     * Fetches all users.
     *
     * @return list of all users
     */
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        return repo.findAll();
    }

    /**
     * Updates the role of a user.
     *
     * @param id       the user ID
     * @param roleType the new role
     * @return the updated user
     * @throws UserNotFoundException if user does not exist
     */
    public User updateUserRole(Long id, RoleType roleType) {
        logger.info("Updating role for user ID: {} to {}", id, roleType);

        User user = repo.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with ID: {}", id);
                    return new UserNotFoundException("User Not Found");
                });

        user.setRoleType(roleType);
        User updatedUser = repo.save(user);
        logger.info("User role updated for user ID: {}", id);
        return updatedUser;
    }

    /**
     * Updates user details.
     *
     * @param id      the user ID
     * @param request the user update request
     * @return the updated user
     * @throws UserNotFoundException if user does not exist
     */
    public User updateUserDetails(Long id, UserUpdateRequest request) {
        logger.info("Updating details for user ID: {}", id);

        User userDetails = repo.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with ID: {}", id);
                    return new UserNotFoundException("User Not Found");
                });

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

        User updatedUser = repo.save(userDetails);
        logger.info("User details updated for user ID: {}", id);
        return updatedUser;
    }

    /**
     * Fetches user details by ID.
     *
     * @param id the user ID
     * @return the user entity
     * @throws UserNotFoundException if user does not exist
     */
    public User getUserDetailsById(Long id) {
        logger.info("Fetching user details for ID: {}", id);
        return repo.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with ID: {}", id);
                    return new UserNotFoundException("User Not Found");
                });
    }

    /**
     * Fetches users by a list of IDs.
     *
     * @param ids list of user IDs
     * @return list of user entities
     */
    public List<User> getUsersByIds(List<Long> ids) {
        logger.info("Fetching users for IDs: {}", ids);
        return repo.findByIds(ids);
    }
}
