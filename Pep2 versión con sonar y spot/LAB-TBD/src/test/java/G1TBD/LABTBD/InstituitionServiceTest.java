package G1TBD.LABTBD;
import G1TBD.LABTBD.entities.InstitutionEntity;
import G1TBD.LABTBD.repositories.InstitutionRepository;
import G1TBD.LABTBD.services.InstitutionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class InstitutionServiceTest {

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
