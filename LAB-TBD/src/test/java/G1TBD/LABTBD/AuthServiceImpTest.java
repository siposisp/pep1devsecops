package G1TBD.LABTBD;


import G1TBD.LABTBD.auth.entities.AuthResponse;
import G1TBD.LABTBD.auth.entities.LocationRequest;
import G1TBD.LABTBD.auth.entities.LoginRequest;
import G1TBD.LABTBD.auth.entities.RegisterRequest;
import G1TBD.LABTBD.auth.services.AuthServiceImp;
import G1TBD.LABTBD.entities.UserEntity;
import G1TBD.LABTBD.services.UserService;
import G1TBD.LABTBD.config.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImpTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImp authService;

    private UserEntity mockUser;

    @BeforeEach
    void setUp() throws ParseException {
        MockitoAnnotations.openMocks(this);

        // Simular un objeto de usuario con datos de ejemplo
        mockUser = UserEntity.builder()
                .rut("12345678-9")
                .email("test@example.com")
                .name("John")
                .lastname("Doe")
                .password("password123")
                .role("USER")
                .availability(true)
                .build();
    }

    @Test
    void testRegister() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = sdf.parse("2000-01-01");
        LocationRequest locationRequest = new LocationRequest(12.34, 56.78); // Asegúrate de que locationRequest esté inicializado
        RegisterRequest request = RegisterRequest.builder()
                .rut("12345678-9")
                .email("test@example.com")
                .name("John")
                .lastName("Doe")
                .birthDate(birthDate)
                .sex("M")
                .password("password123")
                .role("USER")
                .availability(true)
                .location(locationRequest)
                .build();
        doNothing().when(userService).create(any(UserEntity.class));
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn("jwtToken");
        AuthResponse response = authService.register(request);
        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(userService, times(1)).create(any(UserEntity.class));
        verify(jwtService, times(1)).generateToken(any(UserEntity.class));
    }


    @Test
    void testLogin() {
        LoginRequest request = LoginRequest.builder()
                .rut("12345678-9")
                .password("password123")
                .build();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userService.getByRut("12345678-9")).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(mockUser)).thenReturn("jwtToken");
        AuthResponse response = authService.login(request);
        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));

        verify(userService, times(1)).getByRut("12345678-9");
        verify(jwtService, times(1)).generateToken(mockUser);
    }

}