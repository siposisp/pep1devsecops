package G1TBD.LABTBD;

import G1TBD.LABTBD.entities.EmergencyEntity;
import G1TBD.LABTBD.entities.TaskEntity;
import G1TBD.LABTBD.entities.TaskUserEntity;
import G1TBD.LABTBD.entities.UserEntity;
import G1TBD.LABTBD.repositories.TaskRepository;
import G1TBD.LABTBD.repositories.TaskUserRepository;
import G1TBD.LABTBD.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskUserRepository taskUserRepository;

    @InjectMocks
    private TaskService taskService;

    private TaskEntity task;
    private List<TaskEntity> taskList;
    private TaskUserEntity taskUserEntity;
    private UserEntity user;
    private EmergencyEntity emergency;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        emergency = new EmergencyEntity();
        emergency.setEmergency_id(1L);

        task = new TaskEntity();
        task.setTask_id(1L);
        task.setType("Medical");
        task.setDescription("Emergency Task");
        task.setState(true);
        task.setEmergency(emergency);

        taskList = new ArrayList<>();
        taskList.add(task);

        user = new UserEntity();

        taskUserEntity = new TaskUserEntity();
        taskUserEntity.setUser(user);
        taskUserEntity.setTask(task);
    }

    @Test
    public void testCreate() {
        taskService.create(task);
        verify(taskRepository).create(1L, "Medical", "Emergency Task", true);
    }

    @Test
    public void testUpdate() {
        taskService.update(task);
        verify(taskRepository).update(1L, 1L, "Medical", "Emergency Task", true);
    }

    @Test
    public void testGetAll() {
        when(taskRepository.getAll()).thenReturn(taskList);
        List<TaskEntity> tasks = taskService.getAll();
        verify(taskRepository).getAll();
        assert tasks.size() == 1;
    }

    @Test
    public void testGetById() {
        when(taskRepository.getById(1L)).thenReturn(task);
        TaskEntity retrievedTask = taskService.getById(1L);
        verify(taskRepository).getById(1L);
        assert retrievedTask.getTask_id() == 1L;
    }

    @Test
    public void testGetByEmergencyId() {
        when(taskRepository.getByEmergencyId(1L)).thenReturn(taskList);
        List<TaskEntity> tasksByEmergency = taskService.getByEmergencyId(1L);
        verify(taskRepository).getByEmergencyId(1L);
        assert tasksByEmergency.size() == 1;
    }

    @Test
    public void testGetAllVolunteers() {
        List<TaskUserEntity> taskUserEntities = new ArrayList<>();
        taskUserEntities.add(taskUserEntity);
        when(taskUserRepository.getVolunteersByTaskId(1L)).thenReturn(taskUserEntities);
        List<UserEntity> volunteers = taskService.getAllVolunteers(1L);
        verify(taskUserRepository).getVolunteersByTaskId(1L);
        assert volunteers.size() == 1;
    }

    @Test
    public void testGetTasksByVolunteer() {
        List<TaskUserEntity> taskUserEntities = new ArrayList<>();
        taskUserEntities.add(taskUserEntity);
        when(taskUserRepository.getByVolunteer("volunteer")).thenReturn(taskUserEntities);
        List<TaskEntity> tasks = taskService.getTasksByVolunteer("volunteer");
        verify(taskUserRepository).getByVolunteer("volunteer");
        assert tasks.size() == 1;
    }

    @Test
    public void testDelete() {
        taskService.delete(1L);
        verify(taskRepository).delete(1L);
    }
}
