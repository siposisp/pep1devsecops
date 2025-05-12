package G1TBD.LABTBD;

import G1TBD.LABTBD.data.SingleEmergencyData;
import G1TBD.LABTBD.data.point.PointEntity;
import G1TBD.LABTBD.data.point.PointService;
import G1TBD.LABTBD.entities.EmergencyEntity;
import G1TBD.LABTBD.entities.TaskEntity;
import G1TBD.LABTBD.entities.UserEntity;
import G1TBD.LABTBD.repositories.EmergencyRepository;
import G1TBD.LABTBD.services.EmergencyService;
import G1TBD.LABTBD.services.TaskService;
import G1TBD.LABTBD.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmergencyServiceTest {

    @Mock
    private EmergencyRepository emergencyRepository;

    @Mock
    private TaskService taskService;

    @Mock
    private UserService userService;

    @Mock
    private PointService pointService;

    @InjectMocks
    private EmergencyService emergencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        EmergencyEntity emergency = new EmergencyEntity();
        emergency.setTitle("Incendio");
        emergency.setDescription("Incendio forestal");
        emergency.setStatus(true);

        UserEntity user = new UserEntity();
        user.setRut("12345678-9");
        emergency.setCoordinator(user);

        PointEntity point = new PointEntity();
        point.setPoint_id(1L);
        emergency.setLocation(point);

        emergencyService.create(emergency);

        verify(emergencyRepository).create("Incendio", "Incendio forestal", true, "12345678-9", 1L);
    }

    @Test
    void testUpdate() {
        EmergencyEntity emergency = new EmergencyEntity();
        emergency.setEmergency_id(1L);
        emergency.setTitle("Actualizado");
        emergency.setDescription("Descripción nueva");
        emergency.setStatus(false);

        UserEntity user = new UserEntity();
        user.setRut("99999999-9");
        emergency.setCoordinator(user);

        emergencyService.update(emergency);

        verify(emergencyRepository).update(1L, "Actualizado", "Descripción nueva", false, "99999999-9");
    }

    @Test
    void testGetById() {
        EmergencyEntity emergency = new EmergencyEntity();
        emergency.setEmergency_id(1L);
        when(emergencyRepository.getById(1L)).thenReturn(emergency);

        EmergencyEntity result = emergencyService.getById(1L);

        assertEquals(1L, result.getEmergency_id());
    }

    @Test
    void testGetById_whenNotFound_shouldThrowException() {
        when(emergencyRepository.getById(1L)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            emergencyService.getXNearbyVolunteers(1L, 5.0, 5);
        });

        assertEquals("No emergency found with id: 1", exception.getMessage());
    }

    @Test
    void testGetAllVolunteers() {
        TaskEntity task = new TaskEntity();
        task.setTask_id(1L);
        List<TaskEntity> tasks = Collections.singletonList(task);
        List<UserEntity> volunteers = Arrays.asList(new UserEntity(), new UserEntity());

        when(taskService.getByEmergencyId(1L)).thenReturn(tasks);
        when(taskService.getAllVolunteers(1L)).thenReturn(volunteers);

        List<UserEntity> result = emergencyService.getAllVolunteers(1L);

        assertEquals(2, result.size());
    }

    @Test
    void testGetXNearbyVolunteers() {
        EmergencyEntity emergency = new EmergencyEntity();
        PointEntity point = new PointEntity();
        point.setLatitude(-33.0);
        point.setLongitude(-70.0);
        emergency.setLocation(point);

        when(emergencyRepository.getById(1L)).thenReturn(emergency);
        when(userService.getXNearbyVolunteers(-33.0, -70.0, 5.0 / 111.12, 5, "VOLUNTEER", true))
                .thenReturn(List.of(new UserEntity(), new UserEntity()));

        List<UserEntity> volunteers = emergencyService.getXNearbyVolunteers(1L, 5.0, 5);

        assertEquals(2, volunteers.size());
    }

    @Test
    void testGetAllClosedEmergencyData() {
        EmergencyEntity emergency = new EmergencyEntity();
        emergency.setEmergency_id(1L);
        emergency.setTitle("Emergencia cerrada");

        when(emergencyRepository.getAllClosed()).thenReturn(List.of(emergency));
        when(taskService.getByEmergencyId(1L)).thenReturn(List.of(new TaskEntity(), new TaskEntity()));
        when(userService.getByEmergencyId(1L)).thenReturn(List.of(new UserEntity()));

        List<SingleEmergencyData> dataList = emergencyService.getAllClosedEmergencyData();

        assertEquals(1, dataList.size());

        assertEquals("Emergencia cerrada", dataList.get(0).getEmergencyTitle());
        assertEquals(1, dataList.get(0).getVolunteerQuantity());
        assertEquals(2, dataList.get(0).getTaskQuantity());
    }

    @Test
    void testDelete() {
        emergencyService.delete(1L);
        verify(emergencyRepository).delete(1L);
    }

    @Test
    void testGetAll() {
        EmergencyEntity emergency1 = new EmergencyEntity();
        emergency1.setEmergency_id(1L);
        emergency1.setTitle("Incendio");

        EmergencyEntity emergency2 = new EmergencyEntity();
        emergency2.setEmergency_id(2L);
        emergency2.setTitle("Terremoto");

        when(emergencyRepository.getAll()).thenReturn(Arrays.asList(emergency1, emergency2));

        List<EmergencyEntity> result = emergencyService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Incendio", result.get(0).getTitle());
        assertEquals("Terremoto", result.get(1).getTitle());
    }

    @Test
    void testGetAllActive() {
        EmergencyEntity activeEmergency = new EmergencyEntity();
        activeEmergency.setEmergency_id(1L);
        activeEmergency.setTitle("Incendio");
        activeEmergency.setStatus(true);

        when(emergencyRepository.getAllActive()).thenReturn(Collections.singletonList(activeEmergency));

        List<EmergencyEntity> result = emergencyService.getAllActive();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isStatus());
        assertEquals("Incendio", result.get(0).getTitle());
    }

    @Test
    void testGetByLocation() {
        EmergencyEntity emergency = new EmergencyEntity();
        emergency.setEmergency_id(1L);
        emergency.setTitle("Incendio");

        double latitude = -33.0;
        double longitude = -70.0;

        when(emergencyRepository.getByLocation(latitude, longitude)).thenReturn(emergency);

        EmergencyEntity result = emergencyService.getByLocation(latitude, longitude);

        assertNotNull(result);
        assertEquals("Incendio", result.getTitle());
        assertEquals(1L, result.getEmergency_id());
    }

    @Test
    void testGetLatestId() {
        EmergencyEntity emergency = new EmergencyEntity();
        emergency.setEmergency_id(1L);
        emergency.setTitle("Incendio");
        emergency.setDescription("Incendio forestal");
        UserEntity user = new UserEntity();
        user.setRut("12345678-9");
        emergency.setCoordinator(user);

        when(emergencyRepository.findLatestEmergencyId("Incendio", "Incendio forestal", "12345678-9")).thenReturn(emergency);

        EmergencyEntity result = emergencyService.getLatestId(emergency);

        assertNotNull(result);
        assertEquals(1L, result.getEmergency_id());
        assertEquals("Incendio", result.getTitle());
    }


}
