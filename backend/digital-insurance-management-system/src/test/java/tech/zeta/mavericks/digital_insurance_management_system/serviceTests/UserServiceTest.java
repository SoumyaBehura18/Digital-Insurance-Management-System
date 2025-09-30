package tech.zeta.mavericks.digital_insurance_management_system.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tech.zeta.mavericks.digital_insurance_management_system.entity.User;
import tech.zeta.mavericks.digital_insurance_management_system.enums.RoleType;
import tech.zeta.mavericks.digital_insurance_management_system.exception.PolicyNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.exception.UserAlreadyExistException;
import tech.zeta.mavericks.digital_insurance_management_system.exception.UserNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.repository.UserRepository;
import tech.zeta.mavericks.digital_insurance_management_system.service.JWTService;
import tech.zeta.mavericks.digital_insurance_management_system.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock private JWTService jwtService;
    @Mock private AuthenticationManager authManager;
    @Mock private UserRepository userRepository;

    private AutoCloseable closeable;

    private User mockUser;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("John");
        mockUser.setEmail("john@example.com");
        mockUser.setPassword("plainPassword");
        mockUser.setRoleType(RoleType.USER);
    }

    // Test: register success
    @Test
    void testRegisterSuccess() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        User result = userService.register(mockUser);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNotEquals("plainPassword", result.getPassword()); // password should be encoded
        verify(userRepository, times(1)).save(any(User.class));
    }

    // Test: register fails (user already exists)
    @Test
    void testRegisterUserAlreadyExists() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(mockUser);

        assertThrows(UserAlreadyExistException.class, () -> userService.register(mockUser));
    }

    // Test: get all users
    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(mockUser));

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("John", users.get(0).getName());
    }

    // Test: update user role success
    @Test
    void testUpdateUserRoleSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User updated = userService.updateUserRole(1L, RoleType.ADMIN);

        assertEquals(RoleType.ADMIN, updated.getRoleType());
        verify(userRepository, times(1)).save(mockUser);
    }

    // Test: update user role not found
    @Test
    void testUpdateUserRoleNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUserRole(99L, RoleType.ADMIN));
    }

    // Test: get users by ids
    @Test
    void testGetUsersByIds() {
        when(userRepository.findByIds(List.of(1L))).thenReturn(List.of(mockUser));

        List<User> users = userService.getUsersByIds(List.of(1L));

        assertEquals(1, users.size());
        assertEquals("john@example.com", users.get(0).getEmail());
    }
}

