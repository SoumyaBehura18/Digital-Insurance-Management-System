package tech.zeta.mavericks.digital_insurance_management_system.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service for generating, validating, and extracting information from JWT tokens.
 *
 * Handles:
 * - JWT token creation with 30 days expiration.
 * - Extraction of username and expiration from JWT.
 * - Validation of JWT tokens against a UserDetails object.
 * Logging is added for key operations.
 */
@Slf4j
@Service
public class JWTService {

    @Value("${spring.app.jwtSecret}")
    private String secretkey;

    /**
     * Default constructor.
     * Generates a new secret key for HMAC SHA-256 if not provided in application properties.
     */
    public JWTService() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretkey = Base64.getEncoder().encodeToString(sk.getEncoded());
            log.info("JWT secret key generated successfully");
        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to generate JWT secret key", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates a JWT token for the given username with 30 days expiration.
     *
     * @param username Username to include in the token
     * @return Generated JWT token as String
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        long expirationMillis = 1000L * 60 * 60 * 24 * 30; // 30 days

        String token = Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .and()
                .signWith(getKey())
                .compact();

        log.info("Generated JWT token for username={}", username);
        return token;
    }

    /**
     * Converts the secret key string into a SecretKey for signing/verifying JWT.
     *
     * @return SecretKey for JWT
     */
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token JWT token
     * @return Username embedded in the token
     */
    public String extractUserName(String token) {
        String username = extractClaim(token, Claims::getSubject);
        log.info("Extracted username={} from JWT token", username);
        return username;
    }

    /**
     * Extracts a specific claim from a JWT token using a claim resolver function.
     *
     * @param token         JWT token
     * @param claimResolver Function to extract specific claim
     * @param <T>           Type of claim
     * @return Extracted claim
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token JWT token
     * @return Claims object
     */
    private Claims extractAllClaims(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        log.info("Extracted all claims from JWT token");
        return claims;
    }

    /**
     * Validates the JWT token against the provided UserDetails.
     * Checks if the username matches and token is not expired.
     *
     * @param token       JWT token
     * @param userDetails UserDetails to validate against
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        boolean isValid = (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
        log.info("JWT validation for username={} result={}", userName, isValid);
        return isValid;
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token JWT token
     * @return true if expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        boolean expired = extractExpiration(token).before(new Date());
        log.info("JWT token expired={}", expired);
        return expired;
    }

    /**
     * Extracts the expiration date from a JWT token.
     *
     * @param token JWT token
     * @return Expiration date
     */
    private Date extractExpiration(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        log.info("Extracted expiration={} from JWT token", expiration);
        return expiration;
    }
}
