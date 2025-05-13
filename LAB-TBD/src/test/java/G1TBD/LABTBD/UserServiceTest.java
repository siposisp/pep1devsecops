package G1TBD.LABTBD;

import G1TBD.LABTBD.data.point.PointEntity;
import G1TBD.LABTBD.data.point.PointService;
import G1TBD.LABTBD.entities.UserEntity;
import G1TBD.LABTBD.repositories.UserRepository;
import G1TBD.LABTBD.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PointService pointService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserWithLocation() {
        PointEntity location = new PointEntity();
        location.setLatitude(10.0);
        location.setLongitude(20.0);

        UserEntity user = new UserEntity();
        user.setRut("12345678-9");
        user.setEmail("test@example.com");
        user.setName("John");
        user.setLastname("Doe");
        Date birthdate = Date.from(LocalDate.of(1990, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        user.setBirthdate(birthdate);
        user.setSex("M");
        user.setPassword("password");
        user.setRole("VOLUNTEER");
        user.setAvailability(true);
        user.setLocation(location);

        when(pointService.findByLatitudeAndLongitude(10.0, 20.0)).thenReturn(1L);
        when(pointService.getById(1L)).thenReturn(location);

        userService.create(user);

        verify(pointService, times(1)).create(any(PointEntity.class));
        verify(userRepository, times(1)).create(
                "12345678-9", "test@example.com", "John",
                "Doe", birthdate, "M",
                "password", "VOLUNTEER", true, location.getPoint_id()
        );

    }

    @Test
    void testCreateUserWithoutLocationThrowsException() {
        UserEntity user = new UserEntity();
        user.setLocation(null);

        assertThrows(IllegalArgumentException.class, () -> userService.create(user));
    }

    @Test
    void testUpdateUser() {
        UserEntity user = new UserEntity();
        user.setRut("11111111-1");
        user.setEmail("update@test.com");
        user.setName("Updated");
        user.setLastname("User");
        Date birthdate = Date.from(LocalDate.of(1985, 5, 5).atStartOfDay(ZoneId.systemDefault()).toInstant());
        user.setBirthdate(birthdate);
        user.setSex("F");
        user.setPassword("updatedpass");
        user.setRole("COORDINATOR");
        user.setAvailability(false);

        userService.update(user);

        verify(userRepository, times(1)).update(
                "11111111-1", "update@test.com", "Updated",
                "User", birthdate, "F",
                "updatedpass", "COORDINATOR", false
        );
    }

    @Test
    void testUpdateLocationByRut() {
        PointEntity location = new PointEntity();
        location.setLatitude(1.0);
        location.setLongitude(2.0);

        userService.updateLocationByRut(location, "12345678-9");

        verify(userRepository, times(1)).updateLocationByRut(location, "12345678-9");
    }

    @Test
    void testGetByRut() {
        UserEntity user = new UserEntity();
        user.setRut("12345678-9");
        when(userRepository.getByRut("12345678-9")).thenReturn(Optional.of(user));

        Optional<UserEntity> result = userService.getByRut("12345678-9");

        assertTrue(result.isPresent());
        assertEquals("12345678-9", result.get().getRut());
    }

    @Test
    void testDeleteUser() {
        userService.delete("98765432-1");

        verify(userRepository, times(1)).delete("98765432-1");
    }

    @Test
    void testGetByEmail() {
        UserEntity user = new UserEntity();
        user.setEmail("user@test.com");

        when(userRepository.getByEmail("user@test.com")).thenReturn(Optional.of(user));

        Optional<UserEntity> result = userService.getByEmail("user@test.com");

        assertTrue(result.isPresent());
        assertEquals("user@test.com", result.get().getEmail());
    }

    @Test
    void testGetByRole() {
        UserEntity user = new UserEntity();
        user.setRole("VOLUNTEER");

        when(userRepository.getByRole("VOLUNTEER")).thenReturn(List.of(user));

        List<UserEntity> result = userService.getByRole("VOLUNTEER");

        assertEquals(1, result.size());
        assertEquals("VOLUNTEER", result.get(0).getRole());
    }

    @Test
    void testGetVolunteers() {
        UserEntity volunteer = new UserEntity();
        volunteer.setRole("VOLUNTEER");

        when(userRepository.getVolunteers()).thenReturn(List.of(volunteer));

        List<UserEntity> result = userService.getVolunteers();

        assertEquals(1, result.size());
        assertEquals("VOLUNTEER", result.get(0).getRole());
    }

    @Test
    void testGetCoordinators() {
        UserEntity coordinator = new UserEntity();
        coordinator.setRole("COORDINATOR");

        when(userRepository.getCoordinators()).thenReturn(List.of(coordinator));

        List<UserEntity> result = userService.getCoordinators();

        assertEquals(1, result.size());
        assertEquals("COORDINATOR", result.get(0).getRole());
    }

    @Test
    void testGetByAvailability() {
        UserEntity availableUser = new UserEntity();
        availableUser.setAvailability(true);

        when(userRepository.getByAvailability(true)).thenReturn(List.of(availableUser));

        List<UserEntity> result = userService.getByAvailability(true);

        assertEquals(1, result.size());
        assertTrue(result.get(0).isAvailability());
    }

    @Test
    void testGetXNearbyVolunteers() {
        UserEntity user = new UserEntity();
        user.setName("Nearby Volunteer");

        when(userRepository.getXNearbyUsersFromPoint(10.0, 20.0, 5.0, 3, "VOLUNTEER", true))
                .thenReturn(List.of(user));

        List<UserEntity> result = userService.getXNearbyVolunteers(10.0, 20.0, 5.0, 3, "VOLUNTEER", true);

        assertEquals(1, result.size());
        assertEquals("Nearby Volunteer", result.get(0).getName());
    }

    @Test
    void testGetByEmergencyId() {
        UserEntity user = new UserEntity();
        user.setRut("12345678-9");

        when(userRepository.getByEmergencyId(1L)).thenReturn(List.of(user));

        List<UserEntity> result = userService.getByEmergencyId(1L);

        assertEquals(1, result.size());
        assertEquals("12345678-9", result.get(0).getRut());
    }

    @Test
    void testGetAllUsers() {
        UserEntity user1 = new UserEntity();
        UserEntity user2 = new UserEntity();

        when(userRepository.getAll()).thenReturn(List.of(user1, user2));

        List<UserEntity> result = userService.getAll();

        assertEquals(2, result.size());
    }

}

