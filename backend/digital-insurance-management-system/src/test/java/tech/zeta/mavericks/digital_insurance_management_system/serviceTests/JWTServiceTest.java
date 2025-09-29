package tech.zeta.mavericks.digital_insurance_management_system.serviceTests;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import tech.zeta.mavericks.digital_insurance_management_system.service.JWTService;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceTest {

    private JWTService jwtService;
    private String username = "testuser";

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        jwtService = new JWTService();

        // Ensure the secret key is set (bypassing @Value)
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        var key = keyGen.generateKey();
        jwtService.getClass().getDeclaredFields(); // workaround for secret injection
        try {
            var field = JWTService.class.getDeclaredField("secretkey");
            field.setAccessible(true);
            field.set(jwtService, java.util.Base64.getEncoder().encodeToString(key.getEncoded()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGenerateToken_NotNull() {
        String token = jwtService.generateToken(username);
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testExtractUserName_Success() {
        String token = jwtService.generateToken(username);
        String extractedUsername = jwtService.extractUserName(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void testValidateToken_Valid() {
        String token = jwtService.generateToken(username);
        User userDetails = new User(username, "password", Collections.emptyList());
        assertTrue(jwtService.validateToken(token, userDetails));
    }

    @Test
    void testValidateToken_InvalidUsername() {
        String token = jwtService.generateToken(username);
        User userDetails = new User("wronguser", "password", Collections.emptyList());
        assertFalse(jwtService.validateToken(token, userDetails));
    }

    @Test
    void testIsTokenExpired_False() throws Exception {
        String token = jwtService.generateToken(username);
        var method = JWTService.class.getDeclaredMethod("isTokenExpired", String.class);
        method.setAccessible(true);
        boolean expired = (boolean) method.invoke(jwtService, token);
        assertFalse(expired);
    }

    @Test
    void testExtractExpiration_Success() throws Exception {
        String token = jwtService.generateToken(username);
        var method = JWTService.class.getDeclaredMethod("extractExpiration", String.class);
        method.setAccessible(true);
        var expiration = (java.util.Date) method.invoke(jwtService, token);
        assertTrue(expiration.after(new java.util.Date()));
    }

    @Test
    void testExtractAllClaims_Success() throws Exception {
        String token = jwtService.generateToken(username);
        var method = JWTService.class.getDeclaredMethod("extractAllClaims", String.class);
        method.setAccessible(true);
        var claims = method.invoke(jwtService, token);
        assertNotNull(claims);
    }

    @Test
    void testInvalidTokenThrowsException() {
        String invalidToken = "this.is.invalid";
        assertThrows(RuntimeException.class, () -> jwtService.extractUserName(invalidToken));
    }
}
