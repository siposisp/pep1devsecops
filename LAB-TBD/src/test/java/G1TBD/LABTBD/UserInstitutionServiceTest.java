package G1TBD.LABTBD;

import G1TBD.LABTBD.entities.UserInstitutionEntity;
import G1TBD.LABTBD.repositories.UserInstitutionRepository;
import G1TBD.LABTBD.services.UserInstitutionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserInstitutionServiceTest {

    @Mock
    private UserInstitutionRepository userInstitutionRepository;

    @InjectMocks
    private UserInstitutionService userInstitutionService;

    private UserInstitutionEntity userInstitution;
    private List<UserInstitutionEntity> userInstitutionList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userInstitution = new UserInstitutionEntity();
        userInstitution.setUser_institution_id(1L);
        userInstitution.setUser(null);
        userInstitution.setInstitution(null);
        userInstitutionList = new ArrayList<>();
        userInstitutionList.add(userInstitution);
    }

    /*
    @Test
    public void testCreate() {
        userInstitutionService.create(userInstitution);
        verify(userInstitutionRepository).create(any(String.class), any(Long.class));
    }

    @Test
    public void testUpdate() {
        userInstitutionService.update(userInstitution);
        verify(userInstitutionRepository).update(any(Long.class), any(String.class), any(Long.class));
    }
     */

    @Test
    public void testGetAll() {
        when(userInstitutionRepository.getAll()).thenReturn(userInstitutionList);
        List<UserInstitutionEntity> result = userInstitutionService.getAll();
        assertEquals(1, result.size());
        verify(userInstitutionRepository).getAll();
    }

    @Test
    public void testGetById() {
        when(userInstitutionRepository.getById(1L)).thenReturn(userInstitution);
        UserInstitutionEntity result = userInstitutionService.getById(1L);
        assertEquals(1L, result.getUser_institution_id());
        verify(userInstitutionRepository).getById(1L);
    }

    @Test
    public void testGetByRut() {
        when(userInstitutionRepository.getByRut("12345678-9")).thenReturn(userInstitution);
        UserInstitutionEntity result = userInstitutionService.getByRut("12345678-9");
        assertNotNull(result);
        verify(userInstitutionRepository).getByRut("12345678-9");
    }

    @Test
    public void testGetByInstitutionId() {
        when(userInstitutionRepository.getByInstitutionId(1L)).thenReturn(userInstitutionList);
        List<UserInstitutionEntity> result = userInstitutionService.getByInstitutionId(1L);
        assertEquals(1, result.size());
        verify(userInstitutionRepository).getByInstitutionId(1L);
    }

    @Test
    public void testDelete() {
        userInstitutionService.delete(1L);
        verify(userInstitutionRepository).delete(1L);
    }
}

