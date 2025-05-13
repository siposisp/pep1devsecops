package G1TBD.LABTBD;

import G1TBD.LABTBD.config.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private JwtService jwtService;
    private Key signingKey;

    @BeforeEach
    void setUp() {
        String testSecret = "OvmAbqM826GczKY1yF2MyYDjnAVcwKMPnL+OcobQ7bWDwf0IGy1YySsRTepJOI8j";
        jwtService = new JwtService(testSecret);
        signingKey = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(testSecret));
    }

    @Test
    void testGetUsernameFromToken() {
        String token = Jwts.builder()
                .setSubject("user123")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 1 día
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        String username = jwtService.getUsernameFromToken(token);
        assertEquals("user123", username);
    }

    @Test
    void testGetClaim() {
        String token = Jwts.builder()
                .setSubject("user123")
                .claim("name", "John Doe")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        String claimName = jwtService.getClaim(token, claims -> claims.get("name", String.class));
        assertEquals("John Doe", claimName);
    }

    @Test
    void testIsTokenValid() {
        String token = Jwts.builder()
                .setSubject("user123")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user123");

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testIsTokenExpired() {
        String token = Jwts.builder()
                .setSubject("user123")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // 10 minutos
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    void testGenerateToken() {
        UserDetails userDetails = new User("user123", "password", new ArrayList<>());

        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }
}
