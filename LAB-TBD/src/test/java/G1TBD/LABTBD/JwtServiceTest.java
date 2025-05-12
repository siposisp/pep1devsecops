package G1TBD.LABTBD;

import G1TBD.LABTBD.config.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void testGetUsernameFromToken() {
        String token = Jwts.builder()
                .setSubject("user123")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 1 día
                .signWith(jwtService.getSigningKey(), SignatureAlgorithm.HS256) // Usa la misma clave
                .compact();

        // Recuperar el nombre de usuario del token
        String username = jwtService.getUsernameFromToken(token);

        assertEquals("user123", username); // Asegúrate de que el nombre de usuario sea correcto
    }

    @Test
    void testGetClaim() {
        // Generar un token de ejemplo con un claim específico
        String token = Jwts.builder()
                .setSubject("user123")
                .claim("name", "John Doe")
                .signWith(jwtService.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        // Obtener el claim "name" usando el servicio
        String claimName = jwtService.getClaim(token, claims -> claims.get("name", String.class));

        // Verificar que el claim "name" sea el esperado
        assertEquals("John Doe", claimName);
    }

    @Test
    void testIsTokenValid() {
        String token = Jwts.builder()
                .setSubject("user123")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 1 día
                .signWith(jwtService.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user123");

        // Verifica que el token sea válido
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testIsTokenExpired() {
        String token = Jwts.builder()
                .setSubject("user123")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora de expiración
                .signWith(jwtService.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        boolean isExpired = jwtService.isTokenExpired(token);

        assertFalse(isExpired); // Asegúrate de que el token no haya expirado
    }


    @Test
    void testGenerateToken() {
        // Mock de UserDetails con el nombre de usuario "user123"
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user123");

        // Generar un token usando el servicio
        String token = jwtService.generateToken(userDetails);

        // Verificar que el token no sea nulo
        assertNotNull(token);
    }
}
