package G1TBD.LABTBD;

import G1TBD.LABTBD.data.SingleEmergencyData;
import G1TBD.LABTBD.data.point.PointEntity;
import G1TBD.LABTBD.data.point.PointService;
import G1TBD.LABTBD.entities.EmergencyEntity;
import G1TBD.LABTBD.entities.InstitutionEntity;
import G1TBD.LABTBD.entities.TaskEntity;
import G1TBD.LABTBD.entities.UserEntity;
import G1TBD.LABTBD.repositories.EmergencyRepository;
import G1TBD.LABTBD.repositories.InstitutionRepository;
import G1TBD.LABTBD.services.EmergencyService;
import G1TBD.LABTBD.services.InstitutionService;
import G1TBD.LABTBD.services.TaskService;
import G1TBD.LABTBD.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

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

    static class InstitutionServiceTest {

        @Mock
        private InstitutionRepository institutionRepository;

        @InjectMocks
        private InstitutionService institutionService;

        private InstitutionEntity institution;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);

            institution = new InstitutionEntity();
            institution.setInstitution_id(1L);
            institution.setName("Institución de prueba");
        }

        @Test
        void createInstitution() {
            institutionService.create(institution);

            verify(institutionRepository, times(1)).create(institution.getName());
        }

        @Test
        void updateInstitution() {
            institutionService.update(institution);

            verify(institutionRepository, times(1)).update(institution.getInstitution_id(), institution.getName());
        }

        @Test
        void getAllInstitutions() {
            when(institutionRepository.getAll()).thenReturn(Arrays.asList(institution));

            List<InstitutionEntity> institutions = institutionService.getAll();

            assertNotNull(institutions);
            assertEquals(1, institutions.size());
            assertEquals("Institución de prueba", institutions.get(0).getName());
        }

        @Test
        void getInstitutionById() {
            when(institutionRepository.getById(1L)).thenReturn(institution);

            InstitutionEntity result = institutionService.getById(1L);

            assertNotNull(result);
            assertEquals("Institución de prueba", result.getName());
        }

        @Test
        void deleteInstitution() {
            institutionService.delete(1L);

            verify(institutionRepository, times(1)).delete(1L);
        }
    }
}
