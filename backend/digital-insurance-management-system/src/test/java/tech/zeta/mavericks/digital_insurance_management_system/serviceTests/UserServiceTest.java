package tech.zeta.mavericks.digital_insurance_management_system.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.UserUpdateRequest;
import tech.zeta.mavericks.digital_insurance_management_system.entity.User;
import tech.zeta.mavericks.digital_insurance_management_system.enums.RoleType;
import tech.zeta.mavericks.digital_insurance_management_system.exception.PolicyNotFoundException;
import tech.zeta.mavericks.digital_insurance_management_system.exception.UserAlreadyExistException;
import tech.zeta.mavericks.digital_insurance_management_system.repository.UserRepository;
import tech.zeta.mavericks.digital_insurance_management_system.service.JWTService;
import tech.zeta.mavericks.digital_insurance_management_system.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authManager;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_UserSuccess() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.register(user);

        assertNotNull(savedUser.getPassword());
        assertNotEquals("password123", savedUser.getPassword()); // password encoded
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_UserAlreadyExists() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(new User());

        assertThrows(UserAlreadyExistException.class, () -> userService.register(user));
    }

    @Test
    void getAllUsers_ReturnsUsers() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void updateUserRole_Success() {
        User user = new User();
        user.setRoleType(RoleType.USER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updated = userService.updateUserRole(1L, RoleType.ADMIN);

        assertEquals(RoleType.ADMIN, updated.getRoleType());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserRole_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PolicyNotFoundException.class, () -> userService.updateUserRole(1L, RoleType.ADMIN));
    }

    @Test
    void updateUserDetails_Success() {
        User user = new User();
        user.setName("Old Name");
        user.setEmail("old@example.com");

        UserUpdateRequest request = new UserUpdateRequest();
        request.setName("New Name");
        request.setEmail("new@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updated = userService.updateUserDetails(1L, request);

        assertEquals("New Name", updated.getName());
        assertEquals("new@example.com", updated.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserDetails_UserNotFound() {
        UserUpdateRequest request = new UserUpdateRequest();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PolicyNotFoundException.class, () -> userService.updateUserDetails(1L, request));
    }

    @Test
    void getUsersByIds_ReturnsUsers() {
        User u1 = new User();
        User u2 = new User();
        List<User> users = Arrays.asList(u1, u2);

        when(userRepository.findByIds(Arrays.asList(1L, 2L))).thenReturn(users);

        List<User> result = userService.getUsersByIds(Arrays.asList(1L, 2L));

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findByIds(Arrays.asList(1L, 2L));
    }
}
