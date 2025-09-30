package tech.zeta.mavericks.digital_insurance_management_system.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import tech.zeta.mavericks.digital_insurance_management_system.dto.LoginRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.RoleUpdateRequest;
import tech.zeta.mavericks.digital_insurance_management_system.controller.UserController;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.LoginResponse;
import tech.zeta.mavericks.digital_insurance_management_system.entity.User;
import tech.zeta.mavericks.digital_insurance_management_system.entity.UserPrincipal;
import tech.zeta.mavericks.digital_insurance_management_system.enums.RoleType;
import tech.zeta.mavericks.digital_insurance_management_system.service.JWTService;
import tech.zeta.mavericks.digital_insurance_management_system.service.UserService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @Mock
    private Authentication authentication;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    // Test: Register User
    @Test
    void testRegister() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("John");

        when(userService.register(any(User.class))).thenReturn(mockUser);

        User result = userController.register(mockUser);

        assertEquals("John", result.getName());
        verify(userService, times(1)).register(mockUser);
    }

    // Test: Login success
    @Test
    void testLoginSuccess() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@email.com");
        request.setPassword("password");

        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("test@email.com");
        user.setRoleType(RoleType.ADMIN);

        // fixed constructor
        UserPrincipal principal = new UserPrincipal(user);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(jwtService.generateToken("test@email.com")).thenReturn("mockJwtToken");

        ResponseEntity<?> response = userController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Treat response body as a Map
        @SuppressWarnings("unchecked")
        LoginResponse body = (LoginResponse) response.getBody();
        assertNotNull(body);
        assertEquals("mockJwtToken", body.getToken());
        assertEquals(1L, body.getUserId());
    }

    // Test: Login failure
    @Test
    void testLoginFailure() {
        LoginRequest request = new LoginRequest();
        request.setEmail("wrong@email.com");
        request.setPassword("badpass");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        ResponseEntity<?> response = userController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"));
    }

    // Test: Get All Users
    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Alice");

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    // Test: Get Users By Ids - success
    @Test
    void testGetUsersByIds() {
        User user = new User();
        user.setId(1L);

        when(userService.getUsersByIds(List.of(1L))).thenReturn(List.of(user));

        ResponseEntity<List<User>> response = userController.getUsersByIds(List.of(1L));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    // Test: Get Users By Ids - empty list
    @Test
    void testGetUsersByIdsEmpty() {
        ResponseEntity<List<User>> response = userController.getUsersByIds(List.of());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    // Test: Update User Role
    @Test
    void testUpdateUserRole() {
        User user = new User();
        user.setId(1L);
        user.setName("John");

        RoleUpdateRequest request = new RoleUpdateRequest();
        request.setRoleType("ADMIN");

        when(userService.updateUserRole(1L, RoleType.ADMIN)).thenReturn(user);

        ResponseEntity<User> response = userController.updateUserRole(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }
}

