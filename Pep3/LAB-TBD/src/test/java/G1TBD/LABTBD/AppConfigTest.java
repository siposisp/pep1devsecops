package G1TBD.LABTBD;

import G1TBD.LABTBD.config.AppConfig;
import G1TBD.LABTBD.entities.UserEntity;
import G1TBD.LABTBD.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class AppConfigTest {

    private UserRepository userRepository;
    private AppConfig appConfig;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        appConfig = new AppConfig(userRepository);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        // Arrange
        String username = "12345678-9";
        UserEntity mockUser = new UserEntity();
        when(userRepository.getByRut(username)).thenReturn(Optional.of(mockUser));
        UserDetails result = appConfig.userDetailService().loadUserByUsername(username);
        assertNotNull(result);
        verify(userRepository, times(1)).getByRut(username);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        String username = "no-existe";
        when(userRepository.getByRut(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () ->
                appConfig.userDetailService().loadUserByUsername(username)
        );
        verify(userRepository, times(1)).getByRut(username);
    }
}